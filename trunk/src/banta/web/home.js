
var server = mockServer;
var state = {};

function locationDev() {
    return window.location.hostname.startsWith('localhost') || window.location.hostname.startsWith("192.168");    
}

function locationLive() {
    return window.location.hostname.indexOf('appcentral.info') > 0;    
}

$(document).ready(function() {    
    utilInit();
    documentLoad();
});

var loadedComponents = [];
var loadedComponentsLengthRequired = 6;

function documentLoad() {
    console.log('documentLoad');
    googleLoginLoad(documentLoaded);
    personaLoginLoad(documentLoaded);
    chatLoad(documentLoaded);
    chatsLoad(documentLoaded);
    contactsLoad(documentLoaded);
    contactEditLoad(documentLoaded);
}

function documentLoaded(component) {
    loadedComponents.push(component);
    console.log('documentLoaded', loadedComponents.length, component);
    if (loadedComponents.length === loadedComponentsLengthRequired) {
        documentReady();
    }
}

function documentReady() {
    console.log('documentReady');
    $('.home-clickable').click(homeClick);
    $('.reload-clickable').click(reloadClick);
    $('.about-clickable').click(aboutClick);
    $('.contact-clickable').click(contactClick);
    $('.contacts-clickable').click(contactsClick);
    $('.logout-clickable').click(logoutClick);
    window.addEventListener("popstate", function(event) {
        windowState(event);
    });
    server.documentReady();
}

function windowState(event) {
    event.preventDefault();
    windowLocation(window.location.pathname);
} 

function windowClickable() {
    return contactsClickable() && contactEditClickable()
}

function windowLocation(pathname) {
    console.log("windowState", window.location.pathname);
    if (pathname === '/#home') {
        homeClick();
    } else if (pathname === '/#contactUs') {
        contactClick();
    } else if (pathname === '/#aboutUs') {
        aboutClick();
    } else if (!contactsClickable()) {
        console.warn("windowLocation: contacts not ready");
        homeClick();
    } else  if (pathname === '/#contacts') {
       contactsClick();
    } else if (pathname.startsWith('/#contactEdit/')) {
        contactsClick();
    } else if (!contactEditClickable()) {
        console.warn("windowLocation: contactEdit not ready");
        homeClick();
    } else if (pathname === '/#contactAdd') {
        contactAddClick();
    } else if (pathname === '/#contactEdit') {
        contactEditClick();
    } else {
        homeClick();
    }
}

function removeCookies() {
    $.removeCookie('googleAuth');
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
    console.log("loginRes", window.location.pathname);
    if (res.email !== null) {
        state.login = res;
        state.contacts = res.contacts;
        state.chats = res.chats;
        showLoggedInRes();
        windowLocation(window.location.pathname);
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
    window.history.pushState(null, null, "/#aboutUs");
    $('#title').text('About');
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    $("#about-container").show();
}

function homeClick() {
    window.history.pushState(null, null, "/#home");
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
    window.history.pushState(null, null, "/#contactUs");
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
