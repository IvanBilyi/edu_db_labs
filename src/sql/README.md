# SQL-скрипти

### SQL код створення таблиці `study_group`

```sql
CREATE DATABASE IF NOT EXISTS group_db;
USE group_db;

CREATE TABLE study_group (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    year INT NOT NULL
);
```

Ця таблиця має три поля:

- `id` — унікальний ідентифікатор групи у форматі BINARY(16), що зберігає UUID.
- `name` — назва групи, наприклад, "IPZ-21".
- `year` — рік вступу або створення групи.

---

