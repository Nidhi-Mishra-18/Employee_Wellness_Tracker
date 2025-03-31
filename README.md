# Employee_Wellness_Tracker

The **Employee Wellness Tracker** is a web-based application designed to monitor employee well-being through surveys and analytics. Employees can submit periodic wellness surveys, and HR/Admins can analyze the results to improve workplace wellness.

## Purpose
The purpose of this system is to:
- Help employees track their mental and physical well-being.
- Provide HR/Admins with insights and reports on employee wellness.
- Enable CRUD operations for employees, surveys, and reports.

## Features
### User Management
- Admins can create, update, and delete employee profiles.
- Employees can register, log in, and update their profiles.
- Role-based access for Admins and Employees.

### Wellness Survey Management
- Admins can create, update, and delete survey templates.
- Employees can submit new wellness surveys.
- Employees can view, edit, and delete past survey responses (within a time limit).

### Reports & Analytics
- Admins can generate reports on employee wellness trends.
- Reports can be exported as CSV or PDF.

## Technology Stack
- **Backend:** Spring Boot (Java) with REST APIs
- **Frontend:** HTML, CSS, JavaScript
- **Database:** PostgreSQL
- **Authentication:** Role-based authentication (without password encoding for now)

## Installation & Setup
### Prerequisites
- Java 17+
- Spring Boot framework
- PostgreSQL database

### Steps to Run the Application
1. **Clone the repository:**
   ```sh
   git clone https://github.com/Nidhi-Mishra-18/Employee_Wellness_Tracker.git
   cd Employee_Wellness_Tracker
   ```
2. **Set up the database:**
   - Create a new database named `wellness_tracker_db` in PostgreSQL.
   - Update `application.properties` with database credentials.

3. **Run the Backend (Spring Boot):**
   ```sh
   mvn spring-boot:run
   ```

4. **Access the application:**
   - Frontend: `http://localhost:8080/login`

## API Endpoints
  - **Login API**
  - `POST /auth/login` - Login for employees and admins
  
  - **Employee API**
  - `POST /auth/register` - Register a new employee
  - `PUT /auth/update` - Employee can update their profile
  - `PUT /auth/deactivate/{id}` - Employee can deactivate their profile
  
  - **Admin CRUD API**
  - `POST /admin/employees` - Admin can create new employee 
  - `GET /admin/employees` - Retrieves a list of all employees
  - `GET /admin/employees/{id}` - Retrieves a specific employee by their ID
  - `PUT /admin/employees/{id}` - Updates an existing employee's details
  - `DELETE /admin/employees/{id}` - Deletes an employee by their ID
  - `PUT /admin/employees/{id}/role` - Updates an employee's role.
    
  - **Survey Reponses API**
  - `POST /api/responses/submit` - Submits a new survey response
  - `GET /api/responses/history/{employeeId}` - Retrieves the survey response history for a specific employee
  - `DELETE /api/responses/delete/{responseId}` - Deletes a survey response by its ID.
  
  **Survey Template API**
  - `POST /api/surveys/create` - Creates a new survey template
  - `PUT /api/surveys/update/{id}` - Updates an existing survey template by its ID 
  - `DELETE /api/surveys/delete/{id}` - Deletes a survey template by its ID
  - `GET /api/surveys/all` -  Retrieves all survey templates
  
  **Survey Report API**
  - `GET /api/reports/wellness-report` - Get wellness reports
  - `GET /api/reports/exports/csv` - Exports the complete wellness report as a CSV file

## Future Enhancements
- Implement Spring Security for password encoding and authentication.
- Add notification reminders for survey submissions.
- Integrate AI-based insights for predictive analytics on employee wellness.

