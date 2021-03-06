define(['AbstractModel','knockout','plugins/router'], function(AbstractModel,ko,router) {
    return function (id) {
        var self=this;
        AbstractModel.apply(self,[id]);
        self.crudBaseURL="/admin/service/";
        self.serializableFields=["_id","name","address","guestaccess","active","interval","expectedtext"];
        self.name = ko.observable("");
        self.address = ko.observable("");
        self.guestaccess = ko.observable("");
        self.expectedtext = ko.observable("");
        self.interval = ko.observable(1);
        self.lastcheck = ko.observable("");
        self.lastresponse = ko.observable("");
        self.lastresponsecode = ko.observable("");
        self.notified = ko.observable(true);
        self.active=ko.observable(true);
        self.seen=ko.observable("");
        self.seen_formatted='';
        self.seen.subscribe(function(n) {
            if(n!=null) self.seen_formatted=new Date(n).toLocaleString();
        });

        self.ok=ko.observable(true);
        self.notok = ko.computed(function() {
            return !self.ok();
        });

        self.disabled=ko.computed(function() {
            return !self.active();
        });

        self.Edit = function() {
            router.navigate("service/"+self._id());
        };

        self.Cancel = function() {
            router.navigate("");
        };

        self.afterSave = function(r,e) {
            if(!e)
                router.navigate("");
            else alert(r);
        };
    }
});