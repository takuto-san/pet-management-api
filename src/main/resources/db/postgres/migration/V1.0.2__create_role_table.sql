CREATE TYPE role_type AS ENUM (
    'owner', 
    'vet', 
    'nurse', 
    'receptionist', 
    'clinic_admin', 
    'admin'
);

CREATE TABLE roles (
    id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name role_type NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id)
);