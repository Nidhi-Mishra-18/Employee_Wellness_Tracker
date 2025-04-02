const API_URL = "http://localhost:8080/api";
const EMPLOYEE_ID = localStorage.getItem("userId");

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("updateProfileBtn").addEventListener("click", function() {
        window.location.href = "/update_profile.html";
    });

    document.getElementById("logoutBtn").addEventListener("click", function() {
        window.location.href = "/login.html";
    });
});

function deactivateAccount() {
    const userId = localStorage.getItem("userId"); // Get user ID from local storage

    if (!confirm("Are you sure you want to deactivate your account? This action cannot be undone.")) {
        return;
    }

    fetch(`http://localhost:8080/auth/deactivate/${userId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
    })
    .then(response => response.text())
    .then(response => {
        alert(response);
        localStorage.clear(); // Clear user data
        window.location.href = "/login.html"; // Redirect to login page
    })
    .catch(error => console.error("Error deactivating account:", error));
}


// 🟢 Load all surveys
function loadSurveys() {
    fetch(`${API_URL}/surveys/all`)
        .then(response => response.json())
        .then(surveys => {
            const surveyList = document.getElementById("surveyList");
            surveyList.innerHTML = "";
            surveys.forEach(survey => {
                const listItem = document.createElement("li");
                listItem.textContent = survey.title;

                const submitButton = document.createElement("button");
                submitButton.textContent = "Submit Response";
                submitButton.classList.add("sub_response");
                submitButton.onclick = () => loadSurveyForm(survey);


                listItem.appendChild(submitButton);
                surveyList.appendChild(listItem);
            });
        })
        .catch(error => console.error("Error loading surveys:", error));
}

// 🟢 Load survey submission form
function loadSurveyForm(survey) {
    document.getElementById("surveyListContainer").style.display = "none";
    document.getElementById("surveyContainer").style.display = "block";
    document.getElementById("surveyTitle").value = survey.title;
    
    const questionsContainer = document.getElementById("questionsContainer");
    questionsContainer.innerHTML = "";

    survey.questions.forEach((question, index) => {
        const label = document.createElement("label");
        label.textContent = question;
        label.className="question"
        
        const input = document.createElement("select");
        input.className = "surveyAnswer";
        input.required = true;
        input.dataset.index = index;

        // Dropdown with values 1-10
        for (let i = 1; i <= 10; i++) {
            const option = document.createElement("option");
            option.value = i;
            option.textContent = i;
            input.appendChild(option);
        }

        questionsContainer.appendChild(label);
        questionsContainer.appendChild(input);
        questionsContainer.appendChild(document.createElement("br"));
    });

    document.getElementById("surveyForm").onsubmit = function (event) {
        event.preventDefault();
        submitSurveyResponse(survey.id);
    };
}

// 🟢 Submit survey response
function submitSurveyResponse(surveyId) {
    const answers = [...document.querySelectorAll(".surveyAnswer")].map(input => parseInt(input.value, 10));

    const surveyResponse = {
        survey: { id: surveyId },
        employee: { id: EMPLOYEE_ID },
        answers: answers
    };

    fetch(`${API_URL}/responses/submit`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(surveyResponse)
    })
    .then(response => response.json())
    .then(() => {
        alert("Survey Submitted Successfully!");
        loadSurveyHistory(surveyId);
        document.getElementById("surveyContainer").style.display = "none";
        
        document.getElementById("surveyListContainer").style.display = "block";
    })
    .catch(error => console.error("Error submitting survey:", error));
}

// 🟢 Load survey history for an employee
function loadSurveyHistory() {
    fetch(`${API_URL}/responses/history/${EMPLOYEE_ID}`) // Fetch all responses of the employee
        .then(response => response.json())
        .then(responses => {
            const historyContainer = document.getElementById("surveyHistoryContainer");
            historyContainer.innerHTML = ""; // Clear previous content

            const table = document.createElement("table");
            table.border = "1";
            table.style.width = "100%";
            table.style.borderCollapse = "collapse";

            // Create table header
            const thead = document.createElement("thead");
            thead.innerHTML = `
                <tr>
                    <th>Survey</th>
                    <th>Answers</th>
                    <th>Submitted At</th>
                    <th>Actions</th>
                </tr>
            `;
            table.appendChild(thead);

            const tbody = document.createElement("tbody");

            if (responses.length === 0) {
                const noDataRow = document.createElement("tr");
                const noDataCell = document.createElement("td");
                noDataCell.colSpan = 4;
                noDataCell.textContent = "No survey submissions found.";
                noDataCell.style.textAlign = "center";
                noDataRow.appendChild(noDataCell);
                tbody.appendChild(noDataRow);
            } else {
                responses.forEach(response => {
                    const row = document.createElement("tr");

                    const surveyCell = document.createElement("td");
                    surveyCell.textContent = response.survey.title;

                    const answersCell = document.createElement("td");
                    answersCell.textContent = response.answers.join(", "); // Assuming answers is an array

                    const submissionDateCell = document.createElement("td");
                    submissionDateCell.textContent = response.submissionDate;

                    const actionsCell = document.createElement("td");

                    const editButton = document.createElement("button");
                    editButton.classList.add("edit-btn");
                    editButton.textContent = "Edit";
                    editButton.onclick = () => editResponse(response.id, response.answers, response.submissionDate);

                    const deleteButton = document.createElement("button");
                    deleteButton.textContent = "Delete";
                    deleteButton.classList.add("delete-btn");
                    deleteButton.onclick = () => deleteResponse(response.id, response.submissionDate);

                    actionsCell.appendChild(editButton);
                    actionsCell.appendChild(deleteButton);

                    row.appendChild(surveyCell);
                    row.appendChild(answersCell);
                    row.appendChild(submissionDateCell);
                    row.appendChild(actionsCell);

                    tbody.appendChild(row);
                });
            }

            table.appendChild(tbody);
            historyContainer.appendChild(table);
        })
        .catch(error => console.error("Error loading survey history:", error));
}

// 🟢 Check if a response can be edited/deleted within 30 minutes
function canEditOrDelete(submissionDate) {
    const submittedTime = new Date(submissionDate);
    const currentTime = new Date();
    const diffInMinutes = (currentTime - submittedTime) / (1000 * 60);
    return diffInMinutes <= 30;
}

// 🟢 Edit Survey Response (within 30 minutes)
function editResponse(responseId, currentAnswers, submissionDate) {
    if (!canEditOrDelete(submissionDate)) {
        alert("You can only edit your response within 30 minutes.");
        return;
    }

    const form = document.createElement("form");
    form.innerHTML = `<label>Update Answers:</label><br>`;

    currentAnswers.forEach((answer, index) => {
        const label = document.createElement("label");
        label.textContent = `Question ${index + 1}: `;
        label.style.display = "block";
        
        const input = document.createElement("select");
        input.className = "newSurveyAnswer";
        input.dataset.index = index;

        for (let i = 1; i <= 10; i++) {
            const option = document.createElement("option");
            option.value = i;
            option.textContent = i;
            if (i == answer) option.selected = true;
            input.appendChild(option);
        }

        form.appendChild(input);
        form.appendChild(document.createElement("br"));
    });

    const submitButton = document.createElement("button");
    submitButton.textContent = "Update";
    submitButton.type = "submit";
    form.appendChild(submitButton);

    document.body.appendChild(form);

    form.onsubmit = function(event) {
        event.preventDefault();
        const updatedAnswers = [...document.querySelectorAll(".newSurveyAnswer")].map(input => parseInt(input.value, 10));
        updateSurveyResponse(responseId, updatedAnswers);
    };
}

// 🟢 Update survey response
function updateSurveyResponse(responseId, updatedAnswers) {
    fetch(`${API_URL}/responses/update/${responseId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ answers: updatedAnswers })
    })
    .then(response => response.json())
    .then(() => {
        alert("Response updated!");
        loadSurveyHistory();
    })
    .catch(error => console.error("Error updating response:", error));
}

// 🟢 Delete Survey Response (within 30 minutes)
function deleteResponse(responseId, submissionDate) {
    if (!canEditOrDelete(submissionDate)) {
        alert("You can only delete your response within 30 minutes.");
        return;
    }

    if (!confirm("Are you sure you want to delete this response?")) return;

    fetch(`${API_URL}/responses/delete/${responseId}`, { method: "DELETE" })
        .then(() => {
            alert("Response deleted!");
            loadSurveyHistory();
        })
        .catch(error => console.error("Error deleting response:", error));
}

// 🟢 Load Surveys on Page Load
window.onload = function () {
    loadSurveys();
};



        