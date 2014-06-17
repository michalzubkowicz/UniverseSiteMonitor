define(['AbstractModel','knockout','durandal/app'], function(AbstractModel,ko,app) {
    return function (id) {
        var self=this;
        AbstractModel.apply(self,[id]);
        self.crudBaseURL="/admin/response/";
        self.serializableFields=["_id"];
        self.service = ko.observable("");
        self.created = ko.observable("");
        self.response = ko.observable("");
        self.responsecode = ko.observable("");

        self.serviceName = ko.computed(function() {
           return self.service().name;
        });

        self.createdFormatted =ko.computed(function() {
           return new Date(self.created());
        });

        self.viewResponse = function() {
            app.showMessage("<textarea style=\"width:600px; height:300px;\">"+self.response()+"</textarea>");
        }

    }
});