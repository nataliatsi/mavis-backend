-- Criar a tabela de roles
CREATE TABLE tb_roles (
    role_id BIGINT PRIMARY KEY,
    name VARCHAR(25) NOT NULL
);

-- Inserir os roles iniciais
INSERT INTO tb_roles (role_id, name)
VALUES (1, 'ADMIN'), (2, 'BASIC');

-- Criar a tabela de usuários
CREATE TABLE tb_users (
    user_id UUID PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Criar a tabela de associação de usuários com roles
CREATE TABLE tb_users_roles (
    user_id UUID NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES tb_users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES tb_roles(role_id) ON DELETE CASCADE
);


-- Criar a tabela de endereços
CREATE TABLE addresses (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(25) NOT NULL,
    neighborhood VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(2) NOT NULL,
    postal_code VARCHAR(25) NOT NULL,
    reference_point VARCHAR(255)
);

-- Criar a tabela de localização
CREATE TABLE locations (
    id BIGSERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL
);

-- Criar a tabela de perfis de usuários
CREATE TABLE user_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    address_id BIGINT,
    location_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES tb_users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE SET NULL,
    FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE SET NULL
);

-- Criar a tabela de contatos de emergência
CREATE TABLE emergency_contacts (
    id BIGSERIAL PRIMARY KEY,
    user_profile_id BIGINT,
    name VARCHAR(255) NOT NULL,
    relationship VARCHAR(255),
    phone_number VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    FOREIGN KEY (user_profile_id) REFERENCES user_profiles(id) ON DELETE CASCADE
);

-- Criar a tabela de histórico médico
CREATE TABLE medical_history_versions (
    id BIGSERIAL PRIMARY KEY,
    version INT NOT NULL,
    user_profile_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_profile_id) REFERENCES user_profiles(id) ON DELETE CASCADE
);


CREATE TABLE medical_history_allergies (
    medical_history_id BIGINT NOT NULL,
    allergies VARCHAR(255),
    PRIMARY KEY (medical_history_id, allergies),
    FOREIGN KEY (medical_history_id) REFERENCES medical_history_versions(id) ON DELETE CASCADE
);


CREATE TABLE medical_history_medications (
    medical_history_id BIGINT NOT NULL,
    medications VARCHAR(255),
    PRIMARY KEY (medical_history_id, medications),
    FOREIGN KEY (medical_history_id) REFERENCES medical_history_versions(id) ON DELETE CASCADE
);

CREATE TABLE medical_history_pre_existing_conditions (
    medical_history_id BIGINT NOT NULL,
    pre_existing_conditions VARCHAR(255),
    PRIMARY KEY (medical_history_id, pre_existing_conditions),
    FOREIGN KEY (medical_history_id) REFERENCES medical_history_versions(id) ON DELETE CASCADE
);
