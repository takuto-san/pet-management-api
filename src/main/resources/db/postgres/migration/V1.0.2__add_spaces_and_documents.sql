-- Spaces table
CREATE TABLE spaces (
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id    UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    name       TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_spaces_user_id ON spaces (user_id);
CREATE INDEX idx_spaces_created_at ON spaces (created_at);

-- Documents table
CREATE TABLE documents (
    id            UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    space_id      UUID NOT NULL REFERENCES spaces (id) ON DELETE CASCADE,
    parent_doc_id UUID REFERENCES documents (id) ON DELETE CASCADE,
    title         TEXT NOT NULL,
    body          JSONB,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_documents_space_id ON documents (space_id);
CREATE INDEX idx_documents_parent_doc_id ON documents (parent_doc_id);
CREATE INDEX idx_documents_created_at ON documents (created_at);

-- Triggers
CREATE OR REPLACE TRIGGER set_update_at_on_spaces
BEFORE UPDATE ON spaces
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE OR REPLACE TRIGGER set_update_at_on_documents
BEFORE UPDATE ON documents
FOR EACH ROW EXECUTE FUNCTION update_timestamp();
