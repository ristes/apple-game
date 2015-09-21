'use strict';

// Declare app level module which depends on filters, and services
var Game = angular.module('Game', ['Game.filters', 'Game.services',
    'Game.directives', 'ezfb', 'smart-table','ngAudio'
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
            appId: '761102634001845',
            version: 'v2.4'
        });

        // Initialize angular-translate
        $translateProvider.useStaticFilesLoader({
            prefix: '/public/i18n/',
            suffix: '.json'
        });

        $httpProvider.interceptors.push('GameHttpInterceptors');
        $translateProvider.preferredLanguage('en');
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
    'MonthOperations',
    '$timeout',
    function($rootScope, $location, $farmer, BoughtItems, $day, Plantation, StoreItems, MonthOperations, $timeout) {
        $farmer.load();
        BoughtItems.load();
        $rootScope.storeItems = StoreItems.getStoreItems();
        Plantation.load();
        $rootScope.visible = false;
        MonthOperations.load();
        
        $rootScope.next = function() {
            $day.next();
            BoughtItems.load();
            $rootScope.$emit("weather-hide");
        };
        $rootScope.nextWeek = function() {
            $day.nextWeek();
            BoughtItems.load();
            $rootScope.$emit("weather-hide");
        };
        $rootScope.nextMonth = function() {
            $day.nextMonth();
            BoughtItems.load();
            $rootScope.$emit("weather-hide");
        };
        $rootScope.active_operation = false;
        $rootScope.operation_gif = "";
        $rootScope.$on("show-animation-manager", function(_scope,cfg) {
        	$rootScope.$emit("side-hide");
        	$rootScope.$emit("shop-hide");
        	$rootScope.active_operation = true;
        	$rootScope.operation_gif = "/public/images/animations/"+cfg.name+".gif";
        	if (cfg.type==="active-on-last-row") {
        		$rootScope.activeOnLast = true;
        	} else {
        		$rootScope.activeOnLast = false;
        	}
        	$timeout(function() {
        		$rootScope.active_operation = false;
        		$rootScope.$emit("shop-hide")
        		$rootScope.operation_gif = "";
        	},5000);
        });
        $location.path("/");
    }
]);
