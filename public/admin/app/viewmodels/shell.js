define(['plugins/router', 'durandal/app','bootstrap','jquery','viewmodels/passwordModal'], function (router, app,bootstrap,$,PasswordModal) {
    return {
        router: router,
        search: function() {
            //It's really easy to show a message box.
            //You can add custom options too. Also, it returns a promise for the user's response.
            app.showMessage('Search not yet implemented...');
        },
        activate: function () {
            router.map([
                { route: '', title:'Dashboard', moduleId: 'viewmodels/dashboard', nav: true },
                //{ route: 'labels', title:'Labels / Translations', moduleId: 'viewmodels/labelList', nav: true },
                { route: 'labels/:id',         moduleId: 'viewmodels/labelEdit'},
                { route: 'service/:id',         moduleId: 'viewmodels/serviceEdit'},
                { route: 'responses', title:'Responses', moduleId: 'viewmodels/responseList', nav: true }
            ]).buildNavigationModel();
            $('.dropdown-toggle').dropdown();

            return router.activate();
        },
        showPasswordModal: function() {
            PasswordModal.show().then(function(response) {
                showLoading();
                $.ajax({
                    url: "/admin/changepassword",
                    type: "POST",
                    data: {"password":response},
                    context: document.body,
                    success: function () {
                        app.showMessage('Password changed:"' + response + '".');
                    },
                    error: function() {
                        app.showMessage("Cannot save password", "Error", ["Close"], true, { style:  { color: "red", "font-size": "16px" } });
                    },
                    complete: function() {
                        hideLoading();
                    }
                });
            });
        }
    };
});