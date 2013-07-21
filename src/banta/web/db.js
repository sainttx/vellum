
var db = {
    chats: {
        put: function(chat) {
            console.log('db.chats.put', chat);
            
        },
    },
    contacts: {
        put: function(contact) {
            console.log('db.contacts.put', contact);
            if (state.contact) {
                var index = u.array.matchIndexOf(state.contacts, state.contact.name, matchName);
                console.log('put', state.contact.name, index);
                if (index >= 0) {
                    state.contacts[index] = contact;
                }
            } else {
                var index = u.array.matchIndexOf(state.contacts, contact.name, matchName);
                if (index && index >= 0) {
                    console.log('put', contact.name, index);
                    state.contacts[index] = contact;
                } else {
                    state.contacts.push(contact);
                }
            }
        },
    },
    events: {
        put: function(event) {
            console.log('db.events.put', event);
            
        },    
    },
};
