

var personaEmail = null;

function personaLoginClick() {
    console.log("personaLoginClick");
    navigator.id.request();
}

function personaLogoutClick() {
    console.log("personaLogoutClick");
    navigator.id.logout();
}

function personaReady() {
    navigator.id.watch({
        loggedInUser: personaEmail,
        onlogin: personaLogin,
        onlogout: personaLogout
    });
    $('.personaLogin-clickable').click(personaLoginClick);
}

function personaLogin(assertion) {
    console.log("personaLogin");
    console.log("onlogin");
    server.ajax({
        type: 'POST',
        url: '/personaLogin',
        data: {
            assertion: assertion
        },
        success: function(res, status, xhr) {
            personaLoginRes(res);
        },
        error: function(xhr, status, err) {
            personaLoginError();
        }
    });
}

function personaLoginRes(res) {
    console.log("personaLoginRes");
    console.log(res);
    if (res.email !== null) {
        personaEmail = res.email;
        showLoggedInEmail(personaEmail);
    }
}

function personaLoginError() {
    console.log("personaLoginError");
    personaEmail = null;    
}

function personaLogout() {
    console.log("personaLogout");
    personaEmail = null;
    logoutReq();
}
