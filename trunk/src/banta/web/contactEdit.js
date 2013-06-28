
b.contactEdit = {
    loaded: function() {
        b.contactEdit.validator = $('#contactEdit-form').validate(b.contactEdit.validatorConfig);
        $('#contactEdit-save').click(b.contactEdit.save);
        $('#contactEdit-cancel').click(b.contactEdit.cancel);
        $('#contactEdit-cancel').focus(b.contactEdit.cancelFocus);
    },
    validatorConfig: {
        rules: {
            name: {
                minlength: 2,
                required: true,
                sanitary: true
            },
            mobile: {
                minlength: 10,
                maxlength: 10,
                digits: true,
                required: false
            },
            email: {
                required: false,
                email: true
            }
        },
        highlight: function(element) {
            $(element).closest('.control-group').removeClass('success').addClass('error');
            b.contactEdit.buttons(false);
            $(element).focus();
            b.contactEdit.errorElement = element;
        },
        success: function(element) {
            $(element).closest('.control-group').removeClass('error').addClass('success');
            b.contactEdit.buttons(true);
        },
    },
    buttons: function(ok) {
        if (ok) {
            $('#contactEdit-save').addClass('btn-primary');
            $('#contactEdit-cancel').removeClass('btn-primary');
        } else {
            $('#contactEdit-save').removeClass('btn-primary');
            $('#contactEdit-cancel').addClass('btn-primary');
        }
    },
    cancelFocus: function(event) {
        if (b.contactEdit.validator.valid()) {
            $('#contactEdit-save').focus();
        }
    },
    edit: function(contact) {
        state.contact = contact;
        console.log("edit", contact);
        b.contactEdit.clear();
        b.contactEdit.set(contact);
        $('#contactEdit-legend').text('Edit contact');
        showPage('Edit contact', 'contactEdit', 'contactEdit', contact.name);
    },
    newClick: function() {
        state.contact = null;
        b.contactEdit.clear();
        $('#contactEdit-legend').text('New contact');
        showPage('New contact', 'contactEdit', 'contactNew', null);
        b.contactEdit.focus();
    },
    save: function(event) {
        event.preventDefault();
        b.contactEdit.errorElement = null;
        if ($('#contactEdit-form').valid()) {
            var contact = b.contactEdit.get();
            console.log("contactEdit.save", contact);
            db.contacts.put(contact);
            server.ajax({
                url: '/contactEdit',
                data: $('#contactEdit-form').serialize(),
                success: b.contactEdit.res,
                error: b.contactEdit.error,
                memo: contact
            });
        } else {
            b.contactEdit.buttons(false);
        }
    },
    res: function(res) {
        console.log('contactEdit.res');
        console.log(res);
        b.contacts.click();
    },
    error: function() {
        console.log('contactEdit.error');
    },
    cancel: function() {
        console.log("contactEdit.cancel");
        b.contactEdit.clear();
        b.contacts.click();
    },
    clear: function() {
        console.log("contactEdit.clear");
        b.contactEdit.validator.resetForm();
        $('#contactEdit-cancel').addClass('btn-primary');
        $('#contactEdit-save').removeClass('btn-primary');
        b.contactEdit.buttons(false);
        $('#contactEdit-form > fieldset > div.control-group').removeClass('error');
        $('#contactEdit-form > fieldset > div.control-group').removeClass('success');
        b.contactEdit.set({
            name: '',
            mobile: '',
            email: ''
        });
    },
    set: function(o) {
        $('#contactEdit-name-input').val(o.name);
        $('#contactEdit-mobile-input').val(o.mobile);
        $('#contactEdit-email-input').val(o.email);
    },
    get: function() {
        return {
            name: u.string.sanitize($('#contactEdit-name-input').val()),
            mobile: $('#contactEdit-mobile-input').val(),
            email: $('#contactEdit-email-input').val()
        };
    },
    focus: function() {
        $('#contactEdit-name-input').focus();
    },
};


