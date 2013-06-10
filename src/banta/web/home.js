
var server = googleServer;
var state = {};

function locationDev() {
    return window.location.hostname.startsWith('localhost') || window.location.hostname.startsWith("192.168");    
}

function locationLive() {
    return window.location.hostname.indexOf('appcentral.info') > 0;    
}

$(document).ready(function() {    
    utilReady();
    console.log(window.location);
    if (locationDev()) {
        documentReadyDev();
    } else if (!redirectDocument()) {
        documentReadyDev();
    }
});

function documentReadyLive() {
    googleLoginLoad();
    personaLoginLoad();
    documentReady();
}

function documentReadyDev() {
    server = mockServer;
    googleLoginReadyMock();
    documentReady();
}

function googleLoginLoad() {
    $.load("https://apis.google.com/js/client.js", function() {
        startClient();
        googleLoginReady();
    });
}

function personaLoginLoad() {
    $.load("https://login.persona.org/include.js", function() {
        personaReady();
    });
}

function documentReady() {
    console.log("documentReady");
    contactsReady();
    contactEditReady();
    $('.home-clickable').click(homeClick);
    $('.reload-clickable').click(reloadClick);
    $('.about-clickable').click(aboutClick);
    $('.contact-clickable').click(contactClick);
    $('.logout-clickable').click(logoutClick);
    window.addEventListener("popstate", function(event) {
        windowState(event);
    });
    initServer();
}

function windowState(event) {
    console.log("windowState", event);
}

function removeCookies() {
    $.removeCookie('googleAuth');
}

function initServer() {
    console.log("initServer");
    if (false) {
        removeCookies();
    }
}

function showPage(name) {
    $('.page-container').hide();
    $('.' + name + '-container').show();
}

function showLanding() {
    showLoggedOut();
}

function showLoggedOut() {
    state.auth = null;
    $('.page-container').hide();
    $('.loggedin-viewable').hide();
    $('.logout-clickable').hide();
    $('.loggedout-viewable').show();
    $("#landing-container").show();
}

function showLoggedInRes() {    
    if (state.auth === null) {
        console.warn('no server auth');
        state.auth = 'unknown';
    }
    notify('Welcome, ' + state.login.email);
    $('#loggedin-message').text("Welcome, " + state.login.name);
    if (false) {
        $('#loggedin-username-clickable').text(state.login.email);
        $('#loggedin-username-clickable').show();
    }
    showLoggedIn();
}

function showLoggedIn() {
    $('.login-clickable').hide();
    $('.page-container').hide();
    $('.loggedout-viewable').hide();
    $('.loggedin-viewable').show();
    $('.logout-clickable').show();
    $('#loggedin-username').show();
    $('#loggedin-info').show();
    $('#welcome-container').show();
}

function loginRes(res) {
    console.log("loginRes");
    if (res.email !== null) {
        state.login = res;
        state.contacts = res.contacts;
        showLoggedInRes();
        contactsClick();
    }
}

function loginError() {
    console.log("googleLoginError");
    showLoggedOut();
}

function logoutClick(event) {
    if (state.auth === 'persona') {
        state.auth = null;
        personaLogoutClick();
    } else {
        state.auth = null;
        logoutReq();
    }
}

function logoutReq() {
    showLoggedOut();
    server.ajax({
        type: 'POST',
        url: '/logout',
        data: null,
        success: logoutRes,
        error: logoutError
    });
}

function logoutRes(res) {
    console.log("logoutRes");
    console.log(res);
}

function logoutError() {
    console.log("logoutError");
}

function aboutClick() {
    window.history.pushState(null, null, "~AboutUs");
    $('#title').text('About');        
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    $("#about-container").show();
}

function homeClick() {
    window.history.pushState(null, null, "~Home");
    $('#title').text('Banta');        
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    if (state.auth === null) {
        $("#landing-container").show();
    } else {
        $("#home-container").show();
    }
}

function contactClick() {
    window.history.pushState(null, null, "~ContactUs");
    $('#title').text('Contact us');
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    $("#contact-container").show();
}

function reloadClick() {
    console.log('reload');
    console.log(window.location.origin);
    if (true) {
        removeCookies();
    }
    window.location.reload();
}

