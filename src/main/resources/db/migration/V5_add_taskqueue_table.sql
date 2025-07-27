CREATE TABLE task_queue (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task_name VARCHAR(255) NOT NULL,
    `status` VARCHAR(255) DEFAULT 'pending' COMMENT "'pending','processing','completed'",
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE task_queue ADD COLUMN priority INT DEFAULT 0;
ALTER TABLE task_queue ADD COLUMN error_message TEXT;
ALTER TABLE task_queue ADD COLUMN retry_count INT DEFAULT 0;