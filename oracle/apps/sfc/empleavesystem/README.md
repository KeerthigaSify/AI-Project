# Sify Technologies – Employee Leave System (OAF)

## Project Overview

| Field             | Details                                              |
|-------------------|------------------------------------------------------|
| Company           | Sify Technologies                                    |
| Project           | Employee Leave System                                |
| DB Prefix         | `xxsify`                                             |
| EBS Version       | Oracle EBS R12                                       |
| DB Host           | 1.6.125.128:1542/AIROLI                              |

---

## Folder Structure

```
oracle/apps/sfc/empleavesystem/
├── db/                         SQL scripts (table, sequence, synonym, lookup)
├── schema/                     Entity Object (EO)
├── server/                     AM, VOs, EVOs
├── webui/                      Page XML files & Controllers
├── lov/server/                 LOV View Object
└── picklist/server/            Picklist View Object
```

---

## Quick Start

1. **Run DB scripts** in order: `01_create_table.sql` → `02_sequence_synonym.sql` → `03_fnd_lookup.sql`
2. **Deploy OAF components** via JDeveloper into the EBS instance OAF base path
3. **Register pages** in Oracle EBS System Admin → Functions & Menus
4. Access via the EBS responsibility assigned to the custom menu

---

## Navigation Flow

```
Oracle EBS Login  →  Search Page  →  (Click Update Icon)  →  Create/Update Page
                                  ↑                                    |
                                  └────────── Cancel / Save ───────────┘
```

## Key Files

| File | Type | Purpose |
|------|------|---------|
| `XxsifyEmpLeaveEO.xml` | Entity Object | Maps to `xxsify_emp_leave_det_t` |
| `XxsifyEmpLeaveAMImpl.java` | App Module | Business logic methods |
| `XxsifyEmpLeaveSearchVO.xml` | View Object | Employee leave search query |
| `XxsifyEmpLeaveCreateVO.xml` | View Object | Insert/Update form VO |
| `XxsifyEmpLeaveSearchPG.xml` | Page XML | Search page layout |
| `XxsifyEmpLeaveCreatePG.xml` | Page XML | Create/Update page layout |
| `XxsifyEmpLeaveSearchCO.java` | Controller | Search page event handling |
| `XxsifyEmpLeaveCreateCO.java` | Controller | Create page event handling |
