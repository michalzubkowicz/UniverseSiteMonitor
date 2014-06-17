define(['AbstractModel','knockout','plugins/router'], function(AbstractModel,ko,router) {
    return function (id) {
        var self=this;
        AbstractModel.apply(self,[id]);
        self.crudBaseURL="/admin/label/";
        self.serializableFields=["_id","name","de","pl","en"];
        self.name = ko.observable("");
        self.pl = ko.observable("");
        self.de = ko.observable("");
        self.en = ko.observable("");

        self.Edit = function() {
            router.navigate("#labels/"+self._id());
        };

        self.Cancel = function() {
            router.navigate("#labels");
        };

        /*
        self.afterRemove = function() {
            //console.log(list);
            //list.labels.remove(self);
        };*/

        self.afterSave = function() {
            router.navigate("#labels");
        };
    }
});