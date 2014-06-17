/*!
 * UniverseSiteMonitor 1.0 Copyright (c) 2014 Universe Surf GmbH & Michal Zubkowicz. All Rights Reserved.
 * Available via Apache 2 license
 * see: https://github.com/michalzubkowicz/UniverseSiteMonitor/ for details
 * contact: michal.zubkowicz@gmail.com
 */

function isDev() {
    return (document.location.hostname.indexOf(".l")>-1 || document.location.hostname=='localhost');
}

function showLoading() {
    $.blockUI({
        css: {
            border: "0px",
            width: "30px",
            height: "30px",
            top: '45%',
            left: '45%',
            backgroundColor: "none"
        },
        overlayCSS: {
            backgroundColor: '#ffffff',
            opacity:.7
        },
        cursor: 'wait',
        message:  '<div id="squircle"><div id="outer-squircle"></div><div id="inner-squircle"></div></div>'
        //message: '<img src="/assets/img/ajax-loader.gif" />'
    });
}


function hideLoading() {
    $.unblockUI();
}

requirejs.config({
    urlArgs: isDev() ? "bust=" +  (new Date()).getTime() : "version=1.0",
    paths: {
        'text': '../lib/require/text',
        'durandal':'../lib/durandal/js',
        'plugins' : '../lib/durandal/js/plugins',
        'transitions' : '../lib/durandal/js/transitions',
        'knockout': '../lib/knockout/knockout-3.1.0',
        'bootstrap': '../lib/template/js/bootstrap',
        'jquery': '../lib/jquery/jquery-1.11.1',
        'pluginjackson': '../knockout.plugin.jackson',
        'AbstractModel': '../lib/AbstractModel',
        'blockUI': '../lib/blockUI/jquery.blockUI'
    },
    shim: {
        'bootstrap': {
            deps: ['jquery'],
            exports: 'jQuery'
       },
        'pluginjackson': {
            deps: ['jquery','knockout'],
            exports: 'pluginJackson'
        },
        'blockUI': {
            deps: ['jquery'],
            exports: 'blockUI'
        }
    }
});

define(['durandal/system', 'durandal/app', 'durandal/viewLocator'],  function (system, app, viewLocator) {
    //>>excludeStart("build", true);
    if(isDev()) system.debug(true);
    //>>excludeEnd("build");

    app.title = 'Universe Site Monitor';

    app.configurePlugins({
        router:true,
        dialog: true
    });

    app.start().then(function() {
        //Replace 'viewmodels' in the moduleId with 'views' to locate the view.
        //Look for partial views in a 'views' folder in the root.
        viewLocator.useConvention();

        //Show the app by setting the root view model for our application with a transition.
        app.setRoot('viewmodels/shell', 'entrance');
    });


});