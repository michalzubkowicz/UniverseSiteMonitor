/*
 * Copyright (c) 2014 Michal Zubkowicz. All Rights Reserved.
 * Available via Apache 2 license
 * contact: michal.zubkowicz@gmail.com
 */

define(['knockout','blockUI',], function(ko) {
    return function(id) {
        var self=this;
        var system = require('durandal/system');
        var app =  require('durandal/app');
        self.crudBaseURL="enter CRUD address as self.crudBaseURL in model";
        self.serializableFields=[];
        self.id=ko.observable(id); //null if new element
        self._id = ko.computed({
            read: function () {
                return self.id();
            },
            write: function (value) {
                self.id(value);
            },
            owner: this
        });
        self.isLoading=ko.observable(false);

        self.isLoading.subscribe(function(){
            if(self.isLoading()) {
                showLoading();
            } else {
                hideLoading();
            }
        });
        self.isNew = ko.computed(function(){
            return isNaN(parseInt(self.id()));
        });

        self.toJS = function() {
            if(self.serializableFields.length==0) throw new Error("Please provide serializableFields property");
            var d={};
            ko.utils.arrayForEach(self.serializableFields,function(f) {
                if(ko.isObservable(self[f])) {
                    var o = ko.utils.unwrapObservable(self[f]);
                    if(o!=null && typeof o == "object" && o.hasOwnProperty("toJS")) {
                        var ob=o.toJS();
                        for (var op in ob) {
                            d[f+"."+op]=ob[op];
                        }
                    }

                    if(o!=null && typeof o == "object" && !o.hasOwnProperty("toJS")) {
                        var c=0;
                        ko.utils.arrayForEach(o,function(ff) {
                            if(ff.hasOwnProperty("toJS")) {
                                var ob=ff.toJS();
                                for (var op in ob) {
                                    d[f+"["+c+"]"+"."+op]=ob[op];
                                }
                            }
                            else {
                                //zwykła lista
                                if(d[f]==undefined) d[f]=[];
                                d[f][c]=ff;
                            }
                            c++;
                        });
                    }

                    if(typeof o != "object" || o==null) {
                        d[f]=o;
                    }


                } else {
                    d[f]=self[f];
                }
            });
            return d;

        };

        self.fromJS = function(item) {
            for (var property in item) {
                if (item.hasOwnProperty(property)) {
                    try {
                        self[property]((item[property]==null || typeof item[property]=="object" ? item[property] : item[property]));
                    } catch(x) {
                        var has_logger = typeof console === "object" && console.log;
                        if(has_logger) console.log("Error occured in deserialization: "+ x.message+" because of property "+ property+ ". Check you data or implement own deserialization function fromJS");
                    }
                }
            }
        };

        self.toJSON = function() {
            return ko.toJSON(self.toJS());
        };

        self.fromJSON = function(data) {
            throw "Please implement fromJSON function";
        };


        self.beforeLoad = function() {
            system.log("Called Trigger: "+self.crudBaseURL+"beforeLoad");
            app.trigger(self.crudBaseURL+"beforeLoad", self);
        };

        self.afterLoad = function(response,error) {
            if(!error) self.fromJS(response);
            system.log("Called Trigger: "+self.crudBaseURL+"afterLoad");
            app.trigger(self.crudBaseURL+"afterLoad", self);
        };

        self.afterCreate = function(response,error) {
            system.log("Called Trigger: "+self.crudBaseURL+"afterCreate");
            app.trigger(self.crudBaseURL+"afterCreate", self);
        };


        self.Load = function() {
            self.isLoading(true);
            self.beforeLoad();
            $.ajax({
                url: self.crudBaseURL+self.id(),
                type: "GET",
                context: document.body,
                success: function (response) {
                    self.afterLoad(response, false);
                    self.isLoading(false);
                },
                error: function(response) {
                    self.afterLoad(response,true);
                    self.isLoading(false);
                }
            });
        };

        self.beforeSave = function() {
            system.log("Called Trigger: "+self.crudBaseURL+"beforeSave");
            app.trigger(self.crudBaseURL+"beforeSave", self);
        };

        self.afterSave = function(response, error) {
            system.log("Called Trigger: "+self.crudBaseURL+"afterSave");
            app.trigger(self.crudBaseURL+"afterSave", self);
        };


        /**
         * @return {boolean}
         */
        self.Save = function() {
            if(self.beforeSave()===false) return false;
            self.isLoading(true);
            $.ajax({
                url: self.crudBaseURL,
                type:"PUT",
                data: self.toJSON(),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                context: document.body,
                success: function (response) {
                    self.isLoading(false);
                    self.fromJS(response);
                    //self.fromJSON(response);
                    self.afterSave(response,false);
                    if(self.isNew()) self.afterCreate(response,false);

                },
                error: function(response) {
                    self.isLoading(false);
                    self.afterSave(response,true);
                    if(self.isNew()) self.afterCreate(response,true);

                }
            });
            return true;
        };

        self.beforeRemove = function() {
            system.log("Called Trigger: "+self.crudBaseURL+"beforeRemove");
            app.trigger(self.crudBaseURL+"beforeRemove", self);
        };

        self.afterRemove = function(response, error) {
            system.log("Called Trigger: "+self.crudBaseURL+"afterRemove");
            app.trigger(self.crudBaseURL+"afterRemove", self);
        };

        self.Remove = function() {
            if(!confirm("Are you sure?")) return false;
            self.isLoading(true);
            self.beforeRemove();
            $.ajax({
                url: self.crudBaseURL+self.id(),
                type: "DELETE",
                context: document.body,
                success: function (response) {
                    self.afterRemove(response, false);
                    self.isLoading(false);
                },
                error: function(response) {
                    self.afterRemove(response,true);
                    self.isLoading(false);
                }
            });

        };
    }
});
