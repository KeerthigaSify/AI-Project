-- ============================================================
-- Sify Technologies – Employee Leave System
-- Script  : 01_create_table.sql
-- Object  : xxsify_emp_leave_det_t
-- DB      : Oracle EBS R12 | Host: 1.6.125.128:1542/AIROLI
-- ============================================================

CREATE TABLE xxsify_emp_leave_det_t (
    -- Primary Key
    leave_id                NUMBER          NOT NULL,

    -- Core Columns
    employee_id             NUMBER          NOT NULL,
    employee_number         VARCHAR2(30)    NOT NULL,
    employee_name           VARCHAR2(240)   NOT NULL,
    leave_type              VARCHAR2(100)   NOT NULL,
    start_date              DATE            NOT NULL,
    end_date                DATE            NOT NULL,
    no_of_days              NUMBER,
    reason                  VARCHAR2(1000)  NOT NULL,
    status                  VARCHAR2(30)    DEFAULT 'PENDING',

    -- WHO Columns
    created_by              NUMBER          NOT NULL,
    creation_date           DATE            NOT NULL,
    last_updated_by         NUMBER          NOT NULL,
    last_update_date        DATE            NOT NULL,
    last_update_login       NUMBER,

    -- DFF (Descriptive Flexfield) Columns
    attribute_category      VARCHAR2(150),
    attribute1              VARCHAR2(150),
    attribute2              VARCHAR2(150),
    attribute3              VARCHAR2(150),
    attribute4              VARCHAR2(150),
    attribute5              VARCHAR2(150),
    attribute6              VARCHAR2(150),
    attribute7              VARCHAR2(150),
    attribute8              VARCHAR2(150),
    attribute9              VARCHAR2(150),
    attribute10             VARCHAR2(150),
    attribute11             VARCHAR2(150),
    attribute12             VARCHAR2(150),
    attribute13             VARCHAR2(150),
    attribute14             VARCHAR2(150),
    attribute15             VARCHAR2(150),

    -- Error Tracking Columns
    error_flag              VARCHAR2(1),
    error_msg               VARCHAR2(4000),

    CONSTRAINT xxsify_emp_leave_t_pk PRIMARY KEY (leave_id)
);

COMMENT ON TABLE  xxsify_emp_leave_det_t               IS 'Sify Employee Leave Details Custom Table';
COMMENT ON COLUMN xxsify_emp_leave_det_t.leave_id       IS 'Primary Key – populated from xxsify_emp_leave_id_s';
COMMENT ON COLUMN xxsify_emp_leave_det_t.employee_id    IS 'Oracle HR Employee ID (FND_GLOBAL.EMPLOYEE_ID)';
COMMENT ON COLUMN xxsify_emp_leave_det_t.status         IS 'Leave Status: PENDING / APPROVED / REJECTED';
