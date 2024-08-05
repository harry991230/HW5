

let username;
let password;
let controllerUri = "/auth/login";
let redirectUri = "/home/"

$('.login-btn').on('click', function () {
    username = $('#floatingUsername').val();
    password = $('#floatingPassword').val();

    if (!username.trim() || !password.trim()) {
        alert("Username and password cannot be empty.");
        return;
    }

    ajaxRequest(
        controllerUri, { username: username, password: password }
    )
    .done(function(res, textStatus, xhr) {
        if (xhr.status === 200) {
            window.location.href = redirectUri;
        }
    })
    .fail(function(errorObj) {
        alert(errorObj.responseJSON.message || "Unknown error occurred");
    });
});
