
function initTest() {
    console.log('initTest');
}

var mockServer = {
    log: function(message) {
        console.log('server log: ' + message);
        $.ajax({
            type: 'POST',
            url: '/log',
            data: message,
            success: function() {
            },
            error: function() {
                //alert('error logging: ' + data);
            }
        });
    },
    ajax: function(req) {
        console.log('mockServer.ajax', req.url, req.data);
        res = mockRes(req);
        console.log("mockServer res", res);
        req.success(res);
    },
    googleLoginAuthorize: function() {
        var res = {
            access_token: 'dummy_access_token'
        };
        googleLoginAuthorizeRes(res);
    },
    googleClient: function() {
    },
    getPlus: function() {
    },
    documentReady: function() {
        mockReady();
    }
};

var mockData = {
    login: {
        name: 'Testy Tester',
        email: 'test@gmail.com',
        picture: '',
        totpSecret: '',
        totpUrl: '',
        qr: '',
    },
    logout: {
        email: 'test@gmail.com'
    },
    contacts: [
        {
            name: 'Joe Soap',
            mobile: '27827779988',
            email: 'joe@gmail.com',
        },
        {
            name: 'Ginger Bread',
            mobile: '2783667300',
            email: 'gingerb@gmail.com',
        },
        {
            name: 'Harry Potter',
            mobile: '2783667400',
            email: 'harryp@gmail.com',
        }
    ],
    orgs: [
        {
            orgId: 1,
            orgUrl: 'biz.net',
            orgCode: 'biz',
            displayName: 'Biz (Pty) Ltd',
            region: 'Western Cape',
            locality: 'Cape Town',
            country: 'South Africa'
        },
        {
            orgId: 2,
            orgUrl: 'other.net',
            orgCode: 'other',
            displayName: 'The Other Company (Pty) Ltd',
            region: 'Western Cape',
            locality: 'Cape Town',
            country: 'South Africa'
        }
    ],
    textMessages: [
        "Hi there",
        "Hello!",
        "How are you?",
        "Good, thanks. What's up?",
        "Are you going to the event tonight.",
        "No, other plans.",
        "Ok",
        "So see you Saturday.",
        "For sure. Ok, bye.",
        "Bye.",
    ],
    chats: [
    ]
};


function ChatMessage(contact, textMessage, time) {
    this.contact = contact;
    this.textMessage = textMessage;
    this.time = time;
    console.log('ChatMessage', contact, textMessage);
}

function Chat(contacts, messages) {
    this.contacts = contacts;
    this.messages = messages;
    console.log('Chat', contacts[0].name, messages.length, messages[0].textMessage);
}

function mockChatMessages(contact, time) {
    var chatMessages = [];
    foreach(mockData.textMessages, function(i, textMessage) {
        var chatMessage = null;
        if (i % 2 === 0) {
            chatMessage = new ChatMessage(contact, textMessage, time + i * 7 * 1000);
        } else {
            chatMessage = new ChatMessage(null, textMessage, time + i * 9 * 1000);
        }
        console.log(chatMessage);
        chatMessages.push(chatMessage);
    });
    console.log('mockBuildChat', contact.name, chatMessages.length, chatMessages[0].textMessage);
    return chatMessages;
}

function mockReady() {
    var time = new Date();
    foreach(mockData.contacts, function(i, contact) {
        console.log('mockReady', contact);
        time = new Date(time.getTime() - 1000 * 999 * i);
        mockData.chats.push(new Chat([contact], mockChatMessages(contact, time)));
    });
    console.log('mockReady', mockData.chats.length, mockData.chats[0]);
}

function mockRes(req) {
    if (req.url === '/googleLogin') {
        mockData.login.contacts = mockData.contacts;
        return mockData.login;
    } else if (req.url === '/personaLogin') {
        mockData.login.contacts = mockData.contacts;
        return mockData.login;
    } else if (req.url === '/logout') {
        return mockData.logout;
    } else if (req.url === '/chatGet') {
        return chat;
    } else if (req.url === '/chatList') {
        return chatList;
    } else if (req.url === '/contactEdit') {
        return req.data;
    } else if (req.url === '/contactList') {
        return mock.contacts;
    }
    return {
        error: 'mockRes ' + req.url
    }
}

