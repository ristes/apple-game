Game.factory('MonthOperations', ['$resource', '$rootScope',
    function($resource, $rootScope) {
        return {
            /**
             * Sadenje
             *
             * 'planting': [{ ico: "/public/images/game/operations/plowing.png",
             * name: 'deep-plowing', price: 2000, duration: 20, order: 1 }, { ico:
             * "/public/images/game/operations/fertilizing.png", name:
             * 'fertilizing', price: 1000, duration: 10, requires: 'fertilizer',
             * order: 2 }, { ico: "/public/images/game/operations/plowing.png",
             * name: 'plowing', price: 2000, duration: 20, order: 3 }], /** Leto
             */
        	
        	'forbiddenOperation' : {'Rainy':[
        		'spraying',
        		'plowing',
        		'harvest'
        	]},
        	
        	
        	
        	 
            'month-1': [{
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 3,
                description: "Испрскај го насадот за заштита од штетници"
            }],
            'month-2': [{
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 5,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }, {
                ico: "/public/images/game/operations/clothing.png",
                name: 'pruning',
                price: 100,
                duration: 50,
                order: 4,
                description: "Кроењето на садниците овозможува поголем род."
            }, {
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 3,
                description: "Испрскај го насадот за заштита од штетници"
            }],
            'month-3': [{
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 1,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires: 'digging',
                order: 2,
                description: 'Копањето го зголемува родот.'
            }, {
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 3,
                description: "Испрскај го насадот за заштита од штетници"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 4,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }, {
                ico: "/public/images/game/operations/clothing.png",
                name: 'pruning',
                price: 100,
                duration: 50,
                order: 5,
                description: "Кроењето на садниците овозможува поголем род."
            }],
            'month-4': [{
                ico: "/public/images/game/operations/irrigation.png",
                name: 'irrigation',
                price: 100,
                duration: -1,
                order: 1,
                description: "Наводнувањето го зголемува родот."
            }, {
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 4,
                description: "Испрскај го насадот за заштита од штетници"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 5,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }, {
                ico: "/public/images/game/operations/clothing.png",
                name: 'pruning',
                price: 100,
                duration: 50,
                order: 4,
                description: "Кроењето на садниците овозможува поголем род."
            }],
            'month-5': [{
                ico: "/public/images/game/operations/irrigation.png",
                name: 'irrigation',
                price: 100,
                duration: -1,
                order: 1,
                description: "Наводнувањето го зголемува родот."
            }, {
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires: 'digging',
                order: 3,
                description: 'Копањето го зголемува родот.'
            }, {
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 4,
                description: "Испрскај го насадот за заштита од штетници"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 5,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }],
            'month-6': [{
                ico: "/public/images/game/operations/irrigation.png",
                name: 'irrigation',
                price: 100,
                duration: -1,
                order: 1,
                description: "Наводнувањето го зголемува родот."
            }, {
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires: 'digging',
                order: 3,
                description: 'Копањето го зголемува родот.'
            }, {
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 4,
                description: "Испрскај го насадот за заштита од штетници"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 5,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }],
            'month-7': [{
                ico: "/public/images/game/operations/irrigation.png",
                name: 'irrigation',
                price: 100,
                duration: -1,
                order: 1,
                description: "Наводнувањето го зголемува родот."
            }, {
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires: 'digging',
                order: 3,
                description: 'Копањето го зголемува родот.'
            }, {
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 4,
                description: "Испрскај го насадот за заштита од штетници"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 5,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }],
            'month-8': [{
                ico: "/public/images/game/operations/irrigation.png",
                name: 'irrigation',
                price: 100,
                duration: -1,
                order: 1,
                description: "Наводнувањето го зголемува родот."
            }, {
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires: 'digging',
                order: 3,
                description: 'Копањето го зголемува родот.'
            }, {
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 4,
                description: "Испрскај го насадот за заштита од штетници"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 5,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }],
            'month-9': [{
                ico: "/public/images/game/operations/harvest.png",
                name: 'harvest',
                price: 100,
                duration: 100,
                order: 1
            }, {
                ico: "/public/images/game/operations/irrigation.png",
                name: 'irrigation',
                price: 100,
                duration: -1,
                order: 1,
                description: "Наводнувањето го зголемува родот."
            }, {
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires: 'digging',
                order: 3,
                description: 'Копањето го зголемува родот.'
            }, {
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 4,
                description: "Испрскај го насадот за заштита од штетници"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 5,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }],
            'month-10': [{
                ico: "/public/images/game/operations/harvest.png",
                name: 'harvest',
                price: 100,
                duration: 100,
                order: 1
            }, {
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 5,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }],
            'month-11': [{
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires: 'digging',
                order: 3,
                description: 'Копањето го зголемува родот.'
            }],
            'month-12': [{
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 4,
                description: "Испрскај го насадот за заштита од штетници"
            }],
            'season-4': [{
                ico: "/public/images/game/operations/irrigation.png",
                name: 'irrigation',
                price: 100,
                duration: -1,
                order: 1,
                description: "Наводнувањето го зголемува родот."
            }, {
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires: 'digging',
                order: 3,
                description: 'Копањето го зголемува родот.'
            }, {
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 4,
                description: "Испрскај го насадот за заштита од штетници"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 5,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }],
            /**
             * Prolet
             */
            'season-3': [{
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 1,
                description: "Испрскај го насадот за заштита од штетници"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 2,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }, {
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 3,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires: 'digging',
                order: 4,
                description: 'Копањето го зголемува родот.'
            }, {
                ico: "/public/images/game/operations/irrigation.png",
                name: 'irrigation',
                price: 100,
                duration: -1,
                order: 5,
                description: "Наводнувањето го зголемува родот."
            }],
            /**
             * Esen
             */
            'season-2': [{
                ico: "/public/images/game/operations/harvest.png",
                name: 'harvest',
                price: 100,
                duration: 100,
                order: 1
            }, {
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 3,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }, {
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires: 'digging',
                order: 4,
                description: 'Копањето го зголемува родот.'
            }, {
                ico: "/public/images/game/operations/irrigation.png",
                name: 'irrigation',
                price: 100,
                duration: -1,
                order: 5
            }, {
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 6,
                description: "Испрскај го насадот за заштита од штетници"
            }],
            'season-1': [{
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 1,
                description: "Изорај ја нивата"
            }, {
                ico: "/public/images/game/operations/fertilizing.png",
                name: 'fertilizing',
                price: 1000,
                duration: 10,
                requires: 'fertilizer',
                order: 2,
                description: "Ѓубрењето во точен период го зголемува приходот."
            }, {
                ico: "/public/images/game/operations/clothing.png",
                name: 'pruning',
                price: 100,
                duration: 50,
                order: 4,
                description: "Кроењето на садниците овозможува поголем род."
            }, {
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 3,
                description: "Испрскај го насадот за заштита од штетници"
            }]
        };
    }
]);
