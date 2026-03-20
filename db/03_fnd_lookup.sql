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
-- FIX NOTES:
--   v1: Blank Meaning/LookupCode in LOV – switched to MERGE upsert.
--   v2: ORA-30926 fix – LANGUAGE is part of fnd_lookup_values PK.
--       The MERGE ON clause MUST include LANGUAGE; without it Oracle
--       finds multiple rows for the same lookup_code (one per installed
--       NLS language) and throws "unable to get a stable set of rows".
--       We now DELETE stale rows for other languages and
--       INSERT/UPDATE the row for USERENV('LANG') cleanly.
-- -----------------------------------------------------------------------
DECLARE

    PROCEDURE upsert_lv (
        p_lookup_code IN VARCHAR2,
        p_meaning     IN VARCHAR2,
        p_description IN VARCHAR2
    ) IS
        v_lang VARCHAR2(4) := USERENV('LANG');
    BEGIN
        -- Remove any rows seeded under a different language session so
        -- the MERGE always targets exactly one row (no ORA-30926).
        DELETE FROM fnd_lookup_values
        WHERE  lookup_type    = 'XXSIFY_LEAVE_TYPE'
        AND    lookup_code    = p_lookup_code
        AND    security_group_id = 0
        AND    language      != v_lang;

        -- Now MERGE against the single row for the current language.
        MERGE INTO fnd_lookup_values flv
        USING (SELECT 'XXSIFY_LEAVE_TYPE' AS lookup_type,
                      p_lookup_code       AS lookup_code,
                      0                   AS security_group_id,
                      v_lang              AS language
               FROM   dual) src
        ON (    flv.lookup_type      = src.lookup_type
            AND flv.lookup_code      = src.lookup_code
            AND flv.security_group_id = src.security_group_id
            AND flv.language         = src.language)
        WHEN MATCHED THEN
            -- Always update so meaning is guaranteed to be populated.
            UPDATE SET
                flv.meaning           = p_meaning,
                flv.description       = p_description,
                flv.enabled_flag      = 'Y',
                flv.source_lang       = v_lang,
                flv.last_updated_by   = fnd_global.user_id,
                flv.last_update_date  = SYSDATE,
                flv.last_update_login = fnd_global.login_id
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
                    v_lang,
                    v_lang);

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
