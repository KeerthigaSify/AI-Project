-- ============================================================
-- Sify Technologies – Employee Leave System
-- Script  : 02_sequence_synonym.sql
-- ============================================================

-- Sequence
CREATE SEQUENCE xxsify_emp_leave_id_s
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Synonyms (run as APPS user)
CREATE OR REPLACE SYNONYM xxsify_emp_leave_t
    FOR apps.xxsify_emp_leave_det_t;

CREATE OR REPLACE SYNONYM xxsify_emp_leave_s
    FOR apps.xxsify_emp_leave_id_s;
