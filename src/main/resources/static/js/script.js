//function to add new employee  by admin
function addEmployee() {
    const employee = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        phone: document.getElementById("phone").value,
        department: document.getElementById("department").value,
        password: document.getElementById("password").value,
        role: "EMPLOYEE"
    };

    fetch("http://localhost:8080/admin/employees", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(employee)
    })
    .then(response => response.json())
    .then(data => {
        alert("Employee added successfully!");
        window.location.href = "/manage_employee.html";
    })
    .catch(error => console.error("Error adding employee:", error));
}

//function for logout
function logout() {
    localStorage.clear();  // Clears user session from local storage
    window.location.href = "/login.html";  // Redirect to login page
}

//function for employee registration
function register() {
    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let phone = document.getElementById("phone").value;
    let department = document.getElementById("department").value;
    let password = document.getElementById("password").value;

    if (!name || !email || !phone || !department || !password) {
        alert("All fields are required!");
        return;
    }

    fetch("/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, email, phone, department, password, role: "EMPLOYEE" })
    })
    .then(response => response.text())
    .then(alert)
    .then(() => window.location.href = "/login.html");
}

//function for tabs on the login page
function openTab(tabName) {
    let tabs = document.getElementsByClassName("tabcontent");
    for (let i = 0; i < tabs.length; i++) {
        tabs[i].style.display = "none";
    }
    document.getElementById(tabName).style.display = "block";

    let buttons = document.getElementsByClassName("tab-btn");
    for (let i = 0; i < buttons.length; i++) {
        buttons[i].classList.remove("active");
    }
    event.currentTarget.classList.add("active");
}

//login function
function login(role) {
    let email = document.getElementById(role + "-email").value;
    let password = document.getElementById(role + "-password").value;

    fetch("/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    })
    .then(response => response.json()) // Expecting JSON response
    .then(data => {
        if (data.role) {
            // 🟢 Store Employee Details in Local Storage
            localStorage.setItem("userId", data.id);
            localStorage.setItem("userName", data.name);
            localStorage.setItem("userEmail", data.email);
            localStorage.setItem("userPhone", data.phone);
            localStorage.setItem("userDepartment", data.department);
            localStorage.setItem("userPassword", data.password);
            localStorage.setItem("userRole", data.role);

            // Redirect based on role
            if (data.role === "EMPLOYEE" && role==="EMPLOYEE") {
                alert("Login Successfull. Role :"+role);
                window.location.href = "/employee_dashboard.html";
            } else if (data.role === "ADMIN" && role==="ADMIN") {
                alert("Login Successfull. Role :"+role);
                window.location.href = "/admin_dashboard.html";
            }else{
                alert("Invalid role selected");
            }
        } else {
            alert("Invalid login credentials!");
        }
    })
    .catch(error => {
        alert("Error: " + error.message);
    });
}

