
var clientId = '${clientId}';
var apiKey = '${apiKey}';
var scopes = [
"https://www.googleapis.com/auth/plus.me", 
"https://www.googleapis.com/auth/userinfo.email", 
"https://www.googleapis.com/auth/userinfo.profile"];
    
function croc_about() {
    $(".croc-nav-anchor").removeClass("active");
    $(".croc-info").hide();
    $("#croc-info-about").show();        
}

function croc_home() {
    $(".croc-nav-anchor").removeClass("active");
    $(".croc-info").hide();
    $("#croc-info-landing").show();
}        

function croc_contact() {
    $(".croc-nav-anchor").removeClass("active");
    $(".croc-info").hide();
    $("#croc-info-contact").show();
}

$(document).ready(function() {
    $(".croc-home").click(croc_home);
    $(".croc-about").click(croc_about);
    $(".croc-contact").click(croc_contact);
    autoRedirect();
});

function autoRedirect() {
    var location = window.location;
    if (window.location.protocol != "https:") {
        var host = location.host;
        var index = location.host.indexOf(':');
        if (index > 0) {
            host = location.host.substring(0, index) + ':8443';
        }
        location = "https://" + host + location.pathname + location.search + location.hash;
        console.log(location);
        window.location = location;
    }        
}

function clientLoaded() {
    gapi.client.setApiKey(apiKey);
    window.setTimeout(checkAuth, 1);
}

function checkAuth() {
    gapi.auth.authorize({
        client_id: clientId, 
        scope: scopes, 
        immediate: true
    }, processAuthResult);
}

function clickAuth(event) {
    gapi.auth.authorize({
        client_id: clientId, 
        scope: scopes, 
        immediate: false
    }, processAuthResult);
    return false;
}

function processAuthResult(authResult) {
    if (authResult && !authResult.error) {
        $('.croc-login-clickable').hide();
        $('.croc-login-viewable').hide();
        $('.croc-logout-clickable').hide();
        $('.croc-logout-viewable').hide();
        login(authResult.access_token);
    } else {
        console.log("login required");
        $('.croc-login-viewable').show();
        $('.croc-login-clickable').show();
        $('.croc-login-clickable').click(clickAuth);
    }
}

function getPlus() {
    gapi.client.load('plus', 'v1', function() {
        gapi.client.plus.people.get({
            'userId': 'me'
        }).execute(setMe);
    });
}

function setPlus(me) {
    $('#croc-login').hide();
    $('#croc-username-text').text(me.displayName);
    $('#croc-user-picture').attr('src', me.image.url);            
    $('#croc-username').show();
}
    
function login(accessToken) {
    console.log(accessToken);
    $.post(
        '/login',
        accessToken,
        loginResponse
        );                
}

function loginResponse(res) {
    console.log("login response received")
    if (res.email != null) {
        $('.croc-login-viewable').hide();
        $('.croc-login-clickable').hide();
        $('#croc-username-text').text(res.email);
        $('#croc-user-picture').attr('src', res.picture);            
        $('.croc-logout-viewable').show();
        $('.croc-logout-clickable').show();
        $('.croc-logout-clickable').click(clickLogout);
    }
}

function clickLogout(event) {
    $.post(
        '/logout',
        null,
        logoutResponse
        );                
}

function logoutResponse(res) {
    console.log("logout response received")
    if (res.email != null) {
        $('.croc-login-clickable').show();
        $('.croc-login-viewable').show();
        $('.croc-login-clickable').click(clickAuth);
        $('.croc-logout-viewable').hide();
        $('.croc-logout-clickable').hide();
        $('#croc-username-text').text(null);
        $('#croc-user-picture').attr('src', null);
    }
}

