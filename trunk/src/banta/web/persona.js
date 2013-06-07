

function personaLoginClick() {
    console.log("personaLoginClick");
    navigator.id.request();
}

function personaLogoutClick() {
    console.log("personaLogoutClick");
    navigator.id.logout();
}

function personaReady() {
    if (navigator.id) {
        navigator.id.watch({
            loggedInUser: null,
            onlogin: personaLogin,
            onlogout: logoutReq,
        });
        $('.personaLogin-clickable').click(personaLoginClick);
    }
}

function personaLogin(assertion) {
    console.log("personaLogin");
    console.log("onlogin");
    server.auth = 'persona';
    server.ajax({
        type: 'POST',
        url: '/personaLogin',
        data: {
            assertion: assertion
        },
        success: function(res, status, xhr) {
            loginRes(res);
        },
        error: function(xhr, status, err) {
            loginError();
        }
    });
}
