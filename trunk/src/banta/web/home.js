
var loggedInUser = null;

$(document).ready(function() {
    documentReady();
});

function documentReady() {
    console.log("documentReady");
    initLib();
    contactsReady();
    contactAddReady();
    $('.home-clickable').click(homeClick);
    $('.reload-clickable').click(reloadClick);
    $('.about-clickable').click(aboutClick);
    $('.contact-clickable').click(contactClick);
    $('.loginGoogle-clickable').click(loginGoogleClick);
    $('.logout-clickable').click(logoutClick);
    if (window.location.protocol == "http:") {
        server = serverTest;
    }
    initServer();
}


function removeCookies() {
    $.removeCookie('googleAuth');
}

var serverReal = {
    accessToken: '',
    ajax: function(req) {
        $.ajax(req);
    },
    googleAuthorize: function() {
        gapi.auth.authorize({
            client_id: clientId,
            scope: scopes,
            immediate: true
        }, googleAuthorizeRes);
    },
    googleClient: function() {
        gapi.client.setApiKey(apiKey);
        window.setTimeout(googleAuthorize, 1);
    },
    getPlus: function() {
        gapi.client.load('plus', 'v1', function() {
            gapi.client.plus.people.get({
                'userId': 'me'
            }).execute(setMe);
        });
    },
};

var server = serverReal;

function initServer() {
    console.log("initServer");
    if (false) {
        removeCookies();
    }
    var googleAuthCookie = $.cookie("googleAuth");
    if (googleAuthCookie) {
        console.log("cookie googleAuth " + googleAuthCookie);
        $('.loginGoogle-clickable').hide();
        server.googleAuthorize();
    } else {
        $('.loginGoogle-clickable').show();
    }
}

function loginGoogleClick() {
    server.googleAuthorize();
}

function documentReadyRedirect() {
    console.log("documentReadyRedirect");
    if (!redirectDocument()) {
        documentReady();
    }
}
function redirectDocument() {
    console.log("redirectDocument " + window.location.protocol);
    if (window.location.protocol == "http:") {
        var host = location.host;
        var index = location.host.indexOf(':');
        if (index > 0) {
            host = location.host.substring(0, index) + ':8443';
        }
        window.location = "https://" + host + location.pathname + location.search + location.hash;
        console.log(window.location);
        return true;
    }
    return false;
}

function initLib() {
    if (!String.prototype.format) {
        String.prototype.format = function() {
            var args = arguments;
            return this.replace(/{(\d+)}/g, function(match, number) {
                return args[number];
            });
        };
    }
}

function showLanding() {
    showLoggedOut();
}

function showLoggedOut() {
    $('.page-container').hide();
    $('.loggedin-viewable').hide();
    $('.logout-clickable').hide();
    $("#landing-container").show();
    $('.landing-viewable').show();
    $('.loginGoogle-clickable').show();
    $('.login-viewable').show();
}

function showLoggedIn() {
    $('.landing-viewable').hide();
    $('.login-viewable').hide();
    $('.login-clickable').hide();
    $('.page-container').hide();
    $('.loggedin-viewable').show();
    $('.logout-clickable').show();
    $('#loggedin-username').show();
    $('#loggedin-info').show();
    $('#welcome-container').show();
}

function showReadyAuth() {
    $('.login-clickable').show();
    $('.login-viewable').show();
    $('.loginGoogle-clickable').click(loginGoogleClick);
}

function googleAuthorizeRes(res) {
    console.log(res);
    if (res && !res.error) {
        showBusyAuth();
        googleAuthorizeAccessToken(res.access_token);
    } else {
        showReadyAuth();
        console.log("login required");
    }
}

function googleAuthorizeAccessToken(accessToken) {
    server.accessToken = accessToken;
    console.log(accessToken);
    server.ajax({
        type: 'POST',
        url: '/login',
        data: 'accessToken=' + accessToken,
        success: loginRes,
        error: loginError
    });
}

function showBusyAuth() {
    $('.loginGoogle-clickable').hide();
    $('.login-viewable').hide();
    $('.logout-clickable').hide();
    $('.loggedin-viewable').hide();
}

function loginError() {
    console.log("login error");
    showReadyAuth();
}

function notify(message) {
    console.log(message);
}

function loginRes(res) {
    console.log("login response received")
    if (res.email != null) {
        notify('Welcome, ' + res.name);
        $('#loggedin-username-clickable').text(res.email);
        $('#loggedin-username-clickable').show();
        $.cookie("googleAuth", res.email);
        loggedInUser = res.email;
        showLoggedIn();
    } else {
        console.log(res);
    }
}

function setPlus(me) {
    $('#login').hide();
    $('#username-text').text(me.displayName);
    $('#user-picture').attr('src', me.image.url);
    $('#username').show();
}

function logoutRes(res) {
    loggedInUser = null;
    console.log('logoutRes');
    if (res.email != null) {
        $('#username-text').text(null);
        $('#user-picture').attr('src', null);
        showLoggedOut();
    }
}

function logoutError() {
    console.log('logoutError');
    showLoggedOut();
}

function buildTr(handler, index) {
    var object = handler.list[index];
    var array = handler.columnArray(object);
    var html = "<tr onclick='" + handler.name + "ListRowClick(" + handler.id(object) + ")'>";
    for (var i = 0; i < array.length; i++) {
        html += '<td>' + array[i] + '</td>';
    }
    html += '</tr>';
    console.log(html);
    return html;
}

function buildTable(tbody, handler) {
    tbody.empty();
    for (var i = 0; i < handler.list.length; i++) {
        tbody.append(buildTr(handler, i));
    }
}

function logoutClick(event) {
    server.ajax({
        type: 'POST',
        url: '/logout',
        data: null,
        success: logoutRes,
        error: logoutError
    });
}

function aboutClick() {
    console.log("aboutClick");
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    $("#about-container").show();
}

function homeClick() {
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    if (!loggedInUser) {
        $("#landing-container").show();        
    } else {
        $("#home-container").show();
    }
}

function reloadClick() {
    console.log('reload');
    console.log(window.location);
    if (true) {
        removeCookies();
    }
    window.location = window.location.origin;
}

function contactClick() {
    $('.nav-item').removeClass("active");
    $('.page-container').hide();
    $("#contact-container").show();
}
