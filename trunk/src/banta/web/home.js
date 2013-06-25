
var b = {};
var dom = {};
var state = { env: 'test' };
var server = mockServer;

function locationDev() {
    return window.location.hostname.startsWith('localhost') || window.location.hostname.startsWith("192.168");
}

function locationLive() {
    return window.location.hostname.indexOf('appcentral.info') > 0;
}

$(document).ready(function() {
    u.init();
    documentLoad();
});

function documentLoad() {
    console.log('documentLoad', window.location.hostname);
    state.loadedComponentsCount = 7;
    load('contacts');
    load('contactEdit');
    load('chats');
    load('chat');
    load('chatContacts');
    load('events');
    load('event');
}

function componentsLoaded() {
    chatLoaded();
    chatsLoaded();
    contactsLoaded();
    contactEditLoaded();
    eventsLoaded();
    console.log('componentsLoaded', b.event);
    b.event.loaded();    
    homeLoaded();
}

function load(name) {
    var hostname = '';
    if (window.location.hostname === 'evanx.neocities.org') {
        hostname = 'https://banta.appcentral.info/';
    }
    $('#' + name + '-container').load(hostname + name + '.html', function(responseText) {
        if (name === 'events') {
        }
        documentLoaded(name);
    });
}

function documentLoaded(component) {
    state.loadedComponentsCount--;
    console.log('documentLoaded', state.loadedComponentsCount, component);
    if (state.loadedComponentsCount === 0) {
        documentReady();
    }
}

function homeLoaded() {
    $('.home-clickable').click(homeClick);
    $('.reload-clickable').click(reloadClick);
    $('.about-clickable').click(aboutClick);
    $('.contact-clickable').click(contactClick);
    $('.contacts-clickable').click(contactsClick);
    $('.logout-clickable').click(logoutClick);
    $('.chat-clickable').click(chatClick);
    $('.chats-clickable').click(chatsClick);
    $('#login-submit').click(loginSubmit);    
}

function documentReady() {
    console.log('documentReady');
    componentsLoaded();
    window.addEventListener("popstate", function(event) {
        windowState(event);
    });
    server.documentReady();
}

function windowState(event) {
    console.log("windowState", window.location.pathname);
    event.preventDefault();
    windowLocation(window.location.pathname);
}

function windowLocation(pathname) {
    console.log("windowLocation", pathname, window.location.pathname);
    if (pathname === '/#home') {
        homeClick();
    } else if (pathname === '/#contactUs') {
        contactClick();
    } else if (pathname === '/#events') {
        eventsClick();
    } else if (pathname.startsWith('/#event')) {
        eventsClick();
    } else if (pathname === '/#aboutUs') {
        aboutClick();
    } else if (pathname === '/#contacts') {
        contactsClick();
    } else if (pathname.startsWith('/#contactEdit/')) {
        contactsClick();
    } else if (pathname === '/#contactNew') {
        contactNewClick();
    } else if (pathname === '/#chats') {
        chatsClick();
    } else if (pathname.startsWith('/#chat/')) {
        chatsClick();
    } else {
        homeClick();
    }
}

function removeCookies() {
    $.cookie.remove('googleAuth');
}

function showLanding() {
    showLoggedOut();
}

function loginSubmit() {
    var number = $('#login-id-input').val();
    console.log('loginSubmit', number);
    if (!u.validate.validatePhoneNumber(number)) {
        $('#login-alert-div').text('Invalid phone number');
        $('#login-alert-div').show();
        $('#login-id-input').val('');
        $('#login-id-input').focus();
    } else {
        $('#login-alert-div').hide();
        state.auth = 'number';
        server.ajax({
            type: 'POST',
            url: '/loginNumber',
            data: number,
            success: loginRes,
            error: loginError
        });
    }
}

function showLoggedOut() {
    $('.loggedin-viewable').hide();
    $('.logout-clickable').hide();
    $('.loggedout-viewable').show();
    showLogin();
}

function showLogin() {
    state.auth = null;
    $('#login-alert-div').hide();
    $('#login-id-input').val('');
    showPage('Banta', 'landing', 'landing', null);
    $('#login-id-input').focus();
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
        if (state.auth === 'google') {
            $.cookie("googleAuth", res.email);
        }
        var path = $.cookie('path');
        console.log('loginRes path', path);
        if (!isEmpty(path)) {
            windowLocation(path);
        } else {
            windowLocation(window.location.pathname);
        }
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

function showPageObj(object, id) {
    showPage(object.title, object.page, object.path, id);
}

function showPage(title, page, path, id) {
    $('#title').text(title);
    $('.page-container').hide();
    $('#' + page + '-container').show();
    setPath(path, id);
}

function setPath(path, id) {
    path = '/#' + path;
    if (id) {
        path += '/' + id.replace(/\s+/g, '');
    }
    window.history.pushState(null, null, path);
    $.cookie('path', path);
    console.log('setPath', $.cookie('path'));
}

function aboutClick() {
    setPath('aboutUs');
    $('#title').text('About');
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    $("#about-container").show();
}

function homeClick() {
    state.chat = null;
    state.contact = null;
    state.purpose = null;
    setPath('home');
    $('.btn').removeClass('btn-primary');
    if (isEmpty(state.contacts)) {
    } else if (isEmpty(state.chats)) {
    } else if (state.chats) {
    } else {
    }
    $('#title').text('Banta');
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    if (state.auth === null) {
        $("#landing-container").show();
        $('#login-id-input').focus();
    } else {
        $("#home-container").show();
    }
}

function contactClick() {
    setPath('cotactUs');
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

function compareName(a, b) {
    if (a.name === b.name) {
        return 0;
    } else if (a.name.toLowerCase() > b.name.toLowerCase()) {
        return 1;
    }
    return -1;
}

function matchName(object, name) {
    return object.name === name;
}
