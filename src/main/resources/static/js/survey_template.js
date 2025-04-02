const API_URL = "http://localhost:8080/api/surveys";

//Load surveys on page load
window.onload = function () {
    console.log("JavaScript loaded successfully!");
    loadSurveys();
};

// Fetch and display all surveys
function loadSurveys() {
    fetch(`${API_URL}/all`)
        .then(response => response.json())
        .then(data => {
            const surveyList = document.getElementById("surveyList");
            surveyList.innerHTML = ""; // Clear previous list
            data.forEach(survey => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <strong>${survey.title}</strong> - ${survey.questions.length} Questions
                    <button id="edit_btn" onclick="editSurvey(${survey.id})">Edit</button>
                    <button id="delete_btn" onclick="deleteSurvey(${survey.id})">Delete</button>
                `;
                surveyList.appendChild(li);
            });
        })
        .catch(error => console.error("Error loading surveys:", error));
}

function editSurvey(id) {
    window.location.href = `/update_survey_template.html?surveyId=${id}`;
}

//Handle survey creation
document.getElementById("createSurveyForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent default form submission

    const title = document.getElementById("title").value;
    const questions = [...document.querySelectorAll(".question")].map(q => q.value);

    if (!title.trim() || questions.length === 0 || questions.some(q => !q.trim())) {
        alert("Please enter a title and at least one question.");
        return;
    }

    fetch(`${API_URL}/create`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, questions })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to create survey.");
        }
        return response.json();
    })
    .then(() => {
        alert("Survey Created!");
        document.getElementById("createSurveyForm").reset(); // Clear form
        loadSurveys(); // Refresh survey list
    })
    .catch(error => console.error("Error creating survey:", error));
});

//Delete survey
function deleteSurvey(id) {
    fetch(`${API_URL}/delete/${id}`, { method: "DELETE" })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to delete survey.");
        }
        return response.text();
    })
    .then(() => {
        alert("Survey Deleted!");
        loadSurveys();
    })
    .catch(error => console.error("Error deleting survey:", error));
}

//Add new question field
function addQuestion() {
    const questionContainer = document.getElementById("questionsContainer");
    const input = document.createElement("input");
    input.type = "text";
    input.className = "question";
    input.required = true;
    questionContainer.appendChild(document.createElement("br"));
    questionContainer.appendChild(input);
}
