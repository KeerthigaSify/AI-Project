-- ============================================================
-- Sify Technologies – Employee Leave System
-- Script  : 03_fnd_lookup.sql
-- Lookup  : XXSIFY_LEAVE_TYPE
-- ============================================================

-- Create Lookup Type
BEGIN
    fnd_lookup_types_pkg.insert_row (
        x_lookup_type            => 'XXSIFY_LEAVE_TYPE',
        x_meaning                => 'Sify Leave Types',
        x_description            => 'Leave types used in Sify Employee Leave System',
        x_application_id         => 0,
        x_security_group_id      => 0,
        x_view_application_id    => 0,
        x_customization_level    => 'U'
    );
END;
/

-- Insert Lookup Values
BEGIN
    -- Annual Leave
    fnd_lookup_values_pkg.insert_row (
        x_lookup_type         => 'XXSIFY_LEAVE_TYPE',
        x_lookup_code         => 'ANNUAL',
        x_meaning             => 'Annual Leave',
        x_description         => 'Annual Privilege Leave',
        x_enabled_flag        => 'Y',
        x_start_date_active   => SYSDATE,
        x_end_date_active     => NULL,
        x_application_id      => 0,
        x_security_group_id   => 0,
        x_view_application_id => 0,
        x_territory_code      => NULL,
        x_attribute_category  => NULL,
        x_tag                 => NULL
    );

    -- Sick Leave
    fnd_lookup_values_pkg.insert_row (
        x_lookup_type         => 'XXSIFY_LEAVE_TYPE',
        x_lookup_code         => 'SICK',
        x_meaning             => 'Sick Leave',
        x_description         => 'Medical / Sick Leave',
        x_enabled_flag        => 'Y',
        x_start_date_active   => SYSDATE,
        x_end_date_active     => NULL,
        x_application_id      => 0,
        x_security_group_id   => 0,
        x_view_application_id => 0,
        x_territory_code      => NULL,
        x_attribute_category  => NULL,
        x_tag                 => NULL
    );

    -- Casual Leave
    fnd_lookup_values_pkg.insert_row (
        x_lookup_type         => 'XXSIFY_LEAVE_TYPE',
        x_lookup_code         => 'CASUAL',
        x_meaning             => 'Casual Leave',
        x_description         => 'Casual / Short Notice Leave',
        x_enabled_flag        => 'Y',
        x_start_date_active   => SYSDATE,
        x_end_date_active     => NULL,
        x_application_id      => 0,
        x_security_group_id   => 0,
        x_view_application_id => 0,
        x_territory_code      => NULL,
        x_attribute_category  => NULL,
        x_tag                 => NULL
    );

    -- Maternity Leave
    fnd_lookup_values_pkg.insert_row (
        x_lookup_type         => 'XXSIFY_LEAVE_TYPE',
        x_lookup_code         => 'MATERNITY',
        x_meaning             => 'Maternity Leave',
        x_description         => 'Maternity Leave',
        x_enabled_flag        => 'Y',
        x_start_date_active   => SYSDATE,
        x_end_date_active     => NULL,
        x_application_id      => 0,
        x_security_group_id   => 0,
        x_view_application_id => 0,
        x_territory_code      => NULL,
        x_attribute_category  => NULL,
        x_tag                 => NULL
    );

    -- Paternity Leave
    fnd_lookup_values_pkg.insert_row (
        x_lookup_type         => 'XXSIFY_LEAVE_TYPE',
        x_lookup_code         => 'PATERNITY',
        x_meaning             => 'Paternity Leave',
        x_description         => 'Paternity Leave',
        x_enabled_flag        => 'Y',
        x_start_date_active   => SYSDATE,
        x_end_date_active     => NULL,
        x_application_id      => 0,
        x_security_group_id   => 0,
        x_view_application_id => 0,
        x_territory_code      => NULL,
        x_attribute_category  => NULL,
        x_tag                 => NULL
    );

    COMMIT;
END;
/
