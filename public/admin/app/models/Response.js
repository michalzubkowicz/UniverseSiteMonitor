define(['AbstractModel','knockout','plugins/router'], function(AbstractModel,ko,router) {
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
           return self.created();
        });

    }
});