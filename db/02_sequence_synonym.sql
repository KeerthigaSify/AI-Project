-- ============================================================
-- Sify Technologies – Employee Leave System
-- Script  : 02_sequence_synonym.sql
-- ============================================================
-- STEP 1 – Connect as XXSIFY user and create the sequence
--          CONNECT xxsify/<password>@AIROLI
-- ============================================================

CREATE SEQUENCE xxsify.xxsify_emp_leave_id_s
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================================
-- STEP 2 – Connect as APPS user and create the synonyms
--          CONNECT apps/<password>@AIROLI
-- ============================================================

-- Synonym pointing to the table in XXSIFY schema
CREATE OR REPLACE SYNONYM apps.xxsify_emp_leave_t
    FOR xxsify.xxsify_emp_leave_det_t;

-- Synonym pointing to the sequence in XXSIFY schema
CREATE OR REPLACE SYNONYM apps.xxsify_emp_leave_s
    FOR xxsify.xxsify_emp_leave_id_s;
