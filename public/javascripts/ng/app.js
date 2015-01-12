'use strict';

// Declare app level module which depends on filters, and services
var Game = angular.module('Game', ['Game.filters', 'Game.services',
    'Game.directives', 'ezfb', 'smart-table'
]);

Game.config([
    '$routeProvider',
    '$translateProvider',
    '$httpProvider',
    '$locationProvider',
    'ezfbProvider',
    '$tooltipProvider',
    function($routeProvider, $translateProvider, $httpProvider,
        $locationProvider, ezfbProvider, $tooltipProvider) {

        $tooltipProvider.setTriggers({
            'mouseenter': 'mouseleave'
        });

        $tooltipProvider.options({
            placement: 'bottom'
        });

        ezfbProvider.setInitParams({
            appId: '671566419604666',
            version: 'v2.0'
        });

        // Initialize angular-translate
        $translateProvider.useStaticFilesLoader({
            prefix: '/public/i18n/',
            suffix: '.json'
        });

        $httpProvider.interceptors.push('GameHttpInterceptors');
        $translateProvider.preferredLanguage('mk');
        $translateProvider.useCookieStorage();

    }
]);

Game.run([
    '$rootScope',
    '$location',
    '$farmer',
    'BoughtItems',
    '$day',
    'Plantation',
    'StoreItems',
    'InfoTable',
    function($rootScope, $location, $farmer, BoughtItems, $day, Plantation, StoreItems, InfoTable) {
        $farmer.load();
        BoughtItems.load();
        $rootScope.storeItems = StoreItems.getStoreItems();
        Plantation.load();
        InfoTable.getNews();
        $rootScope.visible = false;
        $rootScope.next = function() {
            $day.next();
            BoughtItems.load();
            InfoTable.getNews();
            $rootScope.$emit("weather-hide");

        };
        $rootScope.nextWeek = function() {
            $day.nextWeek();
            BoughtItems.load();
            InfoTable.getNews();
            $rootScope.$emit("weather-hide");
        };
        $rootScope.nextMonth = function() {
            $day.nextMonth();
            BoughtItems.load();
            InfoTable.getNews();
            $rootScope.$emit("weather-hide");
        };
        $location.path("/");
    }
]);
