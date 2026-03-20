-- ============================================================
-- Sify Technologies – Employee Leave System
-- Script  : 03_fnd_lookup.sql
-- Lookup  : XXSIFY_LEAVE_TYPE
-- Purpose : Create FND Lookup Type and Values for Leave Types
--
-- Fixes applied:
--   1. fnd_lookup_types does NOT have MEANING or DESCRIPTION
--      columns (ORA-00904). Those columns belong in the
--      translation table fnd_lookup_types_tl.
--      Step 1 now inserts into BOTH fnd_lookup_types (keys)
--      and fnd_lookup_types_tl (meaning / description / NLS).
--   2. Mandatory WHO columns populated via fnd_global built-ins.
--   3. fnd_lookup_values correctly includes LANGUAGE, SOURCE_LANG,
--      MEANING, and DESCRIPTION (those columns do exist there).
--   4. Both steps are idempotent – safe to re-run.
-- ============================================================

-- ------------------------------------------------------------
-- Step 1: Create Lookup Type
-- fnd_lookup_types  – stores the key / structural columns
-- fnd_lookup_types_tl – stores translated MEANING & DESCRIPTION
-- ------------------------------------------------------------
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(1)
    INTO   v_count
    FROM   fnd_lookup_types
    WHERE  lookup_type    = 'XXSIFY_LEAVE_TYPE'
    AND    application_id = 0;

    IF v_count = 0 THEN

        -- Insert the lookup type key row
        INSERT INTO fnd_lookup_types (
            lookup_type,
            application_id,
            security_group_id,
            view_application_id,
            customization_level,
            created_by,
            creation_date,
            last_updated_by,
            last_update_date,
            last_update_login
        ) VALUES (
            'XXSIFY_LEAVE_TYPE',
            0,                    -- FND / Application Object Library
            0,
            0,
            'U',                  -- User-customisable
            fnd_global.user_id,
            SYSDATE,
            fnd_global.user_id,
            SYSDATE,
            fnd_global.login_id
        );

        -- Insert the translated meaning / description row
        INSERT INTO fnd_lookup_types_tl (
            lookup_type,
            application_id,
            security_group_id,
            view_application_id,
            language,
            source_lang,
            meaning,
            description,
            created_by,
            creation_date,
            last_updated_by,
            last_update_date,
            last_update_login
        ) VALUES (
            'XXSIFY_LEAVE_TYPE',
            0,
            0,
            0,
            USERENV('LANG'),
            USERENV('LANG'),
            'Sify Leave Types',
            'Leave types used in Sify Employee Leave System',
            fnd_global.user_id,
            SYSDATE,
            fnd_global.user_id,
            SYSDATE,
            fnd_global.login_id
        );

        DBMS_OUTPUT.PUT_LINE('Lookup Type XXSIFY_LEAVE_TYPE created successfully.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Lookup Type XXSIFY_LEAVE_TYPE already exists - skipped.');
    END IF;

    COMMIT;
END;
/

-- ------------------------------------------------------------
-- Step 2: Insert Lookup Values
-- fnd_lookup_values is a single table that holds both the key
-- and translated (MEANING, DESCRIPTION) columns together with
-- LANGUAGE / SOURCE_LANG – no separate _TL table needed here.
-- A local helper is used to keep each value idempotent.
-- ------------------------------------------------------------
-- -----------------------------------------------------------------------
-- FIX NOTE (blank Meaning/LookupCode in LOV):
-- A prior run of this script may have inserted rows with a different
-- NLS LANG value, OR inserted rows where fnd_lookup_values returned
-- the rows but with null MEANING due to a base/TL join mismatch.
-- The procedure below uses MERGE so it both INSERTS missing rows AND
-- UPDATES any existing rows that have a blank/null meaning.
-- The existence check no longer filters on LANGUAGE so it correctly
-- finds rows seeded under any NLS session.
-- -----------------------------------------------------------------------
DECLARE

    PROCEDURE upsert_lv (
        p_lookup_code IN VARCHAR2,
        p_meaning     IN VARCHAR2,
        p_description IN VARCHAR2
    ) IS
    BEGIN
        MERGE INTO fnd_lookup_values flv
        USING (SELECT 'XXSIFY_LEAVE_TYPE'  AS lookup_type,
                      p_lookup_code        AS lookup_code,
                      0                    AS application_id,
                      0                    AS security_group_id
               FROM   dual) src
        ON (    flv.lookup_type    = src.lookup_type
            AND flv.lookup_code    = src.lookup_code
            AND flv.application_id = src.application_id)
        WHEN MATCHED THEN
            -- Repair any rows that already exist but have a blank/null meaning
            UPDATE SET
                flv.meaning           = p_meaning,
                flv.description       = p_description,
                flv.enabled_flag      = 'Y',
                flv.last_updated_by   = fnd_global.user_id,
                flv.last_update_date  = SYSDATE,
                flv.last_update_login = fnd_global.login_id
            WHERE (flv.meaning IS NULL OR TRIM(flv.meaning) IS NULL
                   OR flv.meaning != p_meaning)
        WHEN NOT MATCHED THEN
            INSERT (lookup_type,
                    lookup_code,
                    meaning,
                    description,
                    enabled_flag,
                    start_date_active,
                    end_date_active,
                    application_id,
                    security_group_id,
                    view_application_id,
                    territory_code,
                    attribute_category,
                    tag,
                    created_by,
                    creation_date,
                    last_updated_by,
                    last_update_date,
                    last_update_login,
                    source_lang,
                    language)
            VALUES ('XXSIFY_LEAVE_TYPE',
                    p_lookup_code,
                    p_meaning,
                    p_description,
                    'Y',
                    SYSDATE,
                    NULL,
                    0,
                    0,
                    0,
                    NULL,
                    NULL,
                    NULL,
                    fnd_global.user_id,
                    SYSDATE,
                    fnd_global.user_id,
                    SYSDATE,
                    fnd_global.login_id,
                    USERENV('LANG'),
                    USERENV('LANG'));

        DBMS_OUTPUT.PUT_LINE('  Lookup Value [' || p_lookup_code || '] - upserted.');
    END upsert_lv;

BEGIN
    DBMS_OUTPUT.PUT_LINE('Upserting lookup values for XXSIFY_LEAVE_TYPE...');

    upsert_lv('ANNUAL',    'Annual Leave',    'Annual Privilege Leave');
    upsert_lv('SICK',      'Sick Leave',      'Medical / Sick Leave');
    upsert_lv('CASUAL',    'Casual Leave',    'Casual / Short Notice Leave');
    upsert_lv('MATERNITY', 'Maternity Leave', 'Maternity Leave');
    upsert_lv('PATERNITY', 'Paternity Leave', 'Paternity Leave');

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('All lookup values processed successfully.');
END;
/
