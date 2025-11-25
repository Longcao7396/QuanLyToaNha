-- ============================================================
--  Flyway Migration V3
--  Thêm indexes để cải thiện performance cho các query thường dùng
-- ============================================================

-- Indexes cho bảng user
CREATE INDEX IF NOT EXISTS idx_user_username ON user(username);
CREATE INDEX IF NOT EXISTS idx_user_role ON user(role);
CREATE INDEX IF NOT EXISTS idx_user_email ON user(email);

-- Indexes cho bảng apartment
CREATE INDEX IF NOT EXISTS idx_apartment_no ON apartment(apartment_no);
CREATE INDEX IF NOT EXISTS idx_apartment_resident_owner ON apartment(resident_owner_id);

-- Indexes cho bảng resident
CREATE INDEX IF NOT EXISTS idx_resident_user_id ON resident(user_id);
CREATE INDEX IF NOT EXISTS idx_resident_status ON resident(status);
CREATE INDEX IF NOT EXISTS idx_resident_email ON resident(email);
CREATE INDEX IF NOT EXISTS idx_resident_phone ON resident(phone);

-- Indexes cho bảng complaint (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_complaint_resident ON complaint(resident_id);
-- CREATE INDEX IF NOT EXISTS idx_complaint_status ON complaint(status);
-- CREATE INDEX IF NOT EXISTS idx_complaint_created_date ON complaint(created_date);

-- Indexes cho bảng vehicle (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_vehicle_resident ON vehicle(resident_id);
-- CREATE INDEX IF NOT EXISTS idx_vehicle_number ON vehicle(vehicle_number);

-- Indexes cho bảng bms_system (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_bms_system_type ON bms_system(system_type);
-- CREATE INDEX IF NOT EXISTS idx_bms_system_status ON bms_system(status);
-- CREATE INDEX IF NOT EXISTS idx_bms_system_location ON bms_system(location);

-- Indexes cho bảng maintenance (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_maintenance_system ON maintenance(system_id);
-- CREATE INDEX IF NOT EXISTS idx_maintenance_status ON maintenance(status);
-- CREATE INDEX IF NOT EXISTS idx_maintenance_assigned ON maintenance(assigned_to);
-- CREATE INDEX IF NOT EXISTS idx_maintenance_priority ON maintenance(priority);
-- CREATE INDEX IF NOT EXISTS idx_maintenance_scheduled_date ON maintenance(scheduled_date);

-- Indexes cho bảng security (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_security_reported_by ON security(reported_by);
-- CREATE INDEX IF NOT EXISTS idx_security_assigned_to ON security(assigned_to);
-- CREATE INDEX IF NOT EXISTS idx_security_status ON security(status);
-- CREATE INDEX IF NOT EXISTS idx_security_priority ON security(priority);
-- CREATE INDEX IF NOT EXISTS idx_security_reported_date ON security(reported_date);

-- Indexes cho bảng cleaning (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_cleaning_assigned_to ON cleaning(assigned_to);
-- CREATE INDEX IF NOT EXISTS idx_cleaning_status ON cleaning(status);
-- CREATE INDEX IF NOT EXISTS idx_cleaning_scheduled_date ON cleaning(scheduled_date);
-- CREATE INDEX IF NOT EXISTS idx_cleaning_created_by ON cleaning(created_by);

-- Indexes cho bảng customer_request (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_customer_request_resident ON customer_request(resident_id);
-- CREATE INDEX IF NOT EXISTS idx_customer_request_status ON customer_request(status);
-- CREATE INDEX IF NOT EXISTS idx_customer_request_priority ON customer_request(priority);
-- CREATE INDEX IF NOT EXISTS idx_customer_request_assigned ON customer_request(assigned_to);
-- CREATE INDEX IF NOT EXISTS idx_customer_request_created_date ON customer_request(created_date);

-- Indexes cho bảng admin_task (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_admin_task_assigned_to ON admin_task(assigned_to);
-- CREATE INDEX IF NOT EXISTS idx_admin_task_created_by ON admin_task(created_by);
-- CREATE INDEX IF NOT EXISTS idx_admin_task_status ON admin_task(status);
-- CREATE INDEX IF NOT EXISTS idx_admin_task_priority ON admin_task(priority);
-- CREATE INDEX IF NOT EXISTS idx_admin_task_due_date ON admin_task(due_date);

-- Indexes cho bảng staff (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_staff_code ON staff(staff_code);
-- CREATE INDEX IF NOT EXISTS idx_staff_status ON staff(status);
-- CREATE INDEX IF NOT EXISTS idx_staff_department ON staff(department);

-- Indexes cho bảng attendance (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_attendance_staff ON attendance(staff_id);
-- CREATE INDEX IF NOT EXISTS idx_attendance_date ON attendance(attendance_date);
-- CREATE INDEX IF NOT EXISTS idx_attendance_status ON attendance(status);
-- CREATE INDEX IF NOT EXISTS idx_attendance_staff_date ON attendance(staff_id, attendance_date);

-- Indexes cho bảng shift_schedule (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_shift_schedule_staff ON shift_schedule(staff_id);
-- CREATE INDEX IF NOT EXISTS idx_shift_schedule_date ON shift_schedule(start_date, end_date);

-- Indexes cho bảng contract (bỏ qua nếu bảng không tồn tại)
-- CREATE INDEX IF NOT EXISTS idx_contract_staff ON contract(staff_id);
-- CREATE INDEX IF NOT EXISTS idx_contract_status ON contract(status);
-- CREATE INDEX IF NOT EXISTS idx_contract_dates ON contract(start_date, end_date);

-- Indexes cho bảng utility (đã có một số trong V2, thêm thêm)
CREATE INDEX IF NOT EXISTS idx_utility_type ON utility(utility_type);
CREATE INDEX IF NOT EXISTS idx_utility_status ON utility(status);
CREATE INDEX IF NOT EXISTS idx_utility_period ON utility(period_year, period_month);

-- Indexes cho bảng invoice (đã có một số trong V2, thêm thêm)
CREATE INDEX IF NOT EXISTS idx_invoice_date ON invoice(invoice_date);
CREATE INDEX IF NOT EXISTS idx_invoice_due_date ON invoice(due_date);
CREATE INDEX IF NOT EXISTS idx_invoice_created_by ON invoice(created_by);

-- Indexes cho bảng payment (đã có một số trong V2, thêm thêm)
CREATE INDEX IF NOT EXISTS idx_payment_date ON payment(payment_date);
CREATE INDEX IF NOT EXISTS idx_payment_received_by ON payment(received_by);

-- Indexes cho bảng notification (đã có một số trong V2, thêm thêm)
CREATE INDEX IF NOT EXISTS idx_notification_type ON notification(notification_type);
CREATE INDEX IF NOT EXISTS idx_notification_priority ON notification(priority);
CREATE INDEX IF NOT EXISTS idx_notification_created_by ON notification(created_by);
CREATE INDEX IF NOT EXISTS idx_notification_sent_date ON notification(sent_date);

-- Indexes cho bảng repair_request (đã có một số trong V2, thêm thêm)
CREATE INDEX IF NOT EXISTS idx_repair_request_resident ON repair_request(resident_id);
CREATE INDEX IF NOT EXISTS idx_repair_request_type ON repair_request(repair_type);
CREATE INDEX IF NOT EXISTS idx_repair_request_priority ON repair_request(priority);
CREATE INDEX IF NOT EXISTS idx_repair_request_requested_date ON repair_request(requested_date);

