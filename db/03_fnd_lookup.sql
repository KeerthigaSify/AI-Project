-- ============================================================
-- Sify Technologies – Employee Leave System
-- Script  : 03_fnd_lookup.sql
-- Lookup  : XXSIFY_LEAVE_TYPE
-- Purpose : Create FND Lookup Type and Values for Leave Types
--
-- Fixes applied vs original script:
--   1. Replaced fnd_lookup_types_pkg.insert_row /
--      fnd_lookup_values_pkg.insert_row which required an
--      x_rowid IN OUT param + full WHO columns (causing
--      PLS-00306: wrong number or types of arguments).
--      Direct INSERTs are used instead – the standard Oracle
--      EBS customisation approach.
--   2. Added mandatory WHO columns (created_by, creation_date,
--      last_updated_by, last_update_date, last_update_login)
--      using fnd_global built-ins.
--   3. Added mandatory NLS columns (language, source_lang)
--      required by fnd_lookup_values.
--   4. Made both blocks idempotent via COUNT existence checks
--      so the script can be re-run without duplicate errors.
-- ============================================================

-- ------------------------------------------------------------
-- Step 1: Create Lookup Type
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
        INSERT INTO fnd_lookup_types (
            lookup_type,
            meaning,
            description,
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
            'Sify Leave Types',
            'Leave types used in Sify Employee Leave System',
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
        DBMS_OUTPUT.PUT_LINE('Lookup Type XXSIFY_LEAVE_TYPE created successfully.');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Lookup Type XXSIFY_LEAVE_TYPE already exists - skipped.');
    END IF;

    COMMIT;
END;
/

-- ------------------------------------------------------------
-- Step 2: Insert Lookup Values
-- A local helper procedure is used to keep each value
-- idempotent and avoid code repetition.
-- fnd_lookup_values requires language + source_lang (NLS).
-- USERENV('LANG') returns the session base language (e.g. US).
-- ------------------------------------------------------------
DECLARE

    PROCEDURE insert_lv (
        p_lookup_code IN VARCHAR2,
        p_meaning     IN VARCHAR2,
        p_description IN VARCHAR2
    ) IS
        v_count NUMBER;
    BEGIN
        SELECT COUNT(1)
        INTO   v_count
        FROM   fnd_lookup_values
        WHERE  lookup_type    = 'XXSIFY_LEAVE_TYPE'
        AND    lookup_code    = p_lookup_code
        AND    application_id = 0
        AND    language       = USERENV('LANG');

        IF v_count = 0 THEN
            INSERT INTO fnd_lookup_values (
                lookup_type,
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
                language
            ) VALUES (
                'XXSIFY_LEAVE_TYPE',
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
                USERENV('LANG')
            );
            DBMS_OUTPUT.PUT_LINE('  Lookup Value [' || p_lookup_code || '] created.');
        ELSE
            DBMS_OUTPUT.PUT_LINE('  Lookup Value [' || p_lookup_code || '] already exists - skipped.');
        END IF;
    END insert_lv;

BEGIN
    DBMS_OUTPUT.PUT_LINE('Inserting lookup values for XXSIFY_LEAVE_TYPE...');

    insert_lv('ANNUAL',    'Annual Leave',    'Annual Privilege Leave');
    insert_lv('SICK',      'Sick Leave',      'Medical / Sick Leave');
    insert_lv('CASUAL',    'Casual Leave',    'Casual / Short Notice Leave');
    insert_lv('MATERNITY', 'Maternity Leave', 'Maternity Leave');
    insert_lv('PATERNITY', 'Paternity Leave', 'Paternity Leave');

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('All lookup values processed successfully.');
END;
/
