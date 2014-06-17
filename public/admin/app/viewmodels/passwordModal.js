define(['plugins/dialog', 'knockout'], function (dialog, ko) {
    var PasswordModal = function() {
        this.input = ko.observable('');
    };

    PasswordModal.prototype.ok = function() {
        dialog.close(this, this.input());
    };

    PasswordModal.prototype.canDeactivate = function () {
        return dialog.showMessage('Are you sure you want to set new password?', 'Just Checking...', ['Yes', 'No']);
    };

    PasswordModal.show = function(){
        return dialog.show(new PasswordModal());
    };

    return PasswordModal;
});