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
        'month-1': [],
        'month-2': [ {
            ico: "/public/images/game/operations/plowing.png",
            name: 'plowing',
            price: 2000,
            duration: 20,
            order: 2
          },{
              ico: "/public/images/game/operations/fertilizing.png",
              name: 'fertilizing',
              price: 1000,
              duration: 10,
              requires: 'fertilizer',
              order: 5
            },{
                ico: "/public/images/game/operations/clothing.png",
                name: 'clothing',
                price: 100,
                duration: 50,
                order: 4
              }],
        'month-3': [ {
            ico: "/public/images/game/operations/plowing.png",
            name: 'plowing',
            price: 2000,
            duration: 20,
            order: 1
          },{
              ico: "/public/images/game/operations/digging.png",
              name: 'digging',
              price: 100,
              duration: 50,
              requires:'digging',
              order: 2
            },{
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 3
              },{
                  ico: "/public/images/game/operations/fertilizing.png",
                  name: 'fertilizing',
                  price: 1000,
                  duration: 10,
                  requires: 'fertilizer',
                  order: 4
                },{
                    ico: "/public/images/game/operations/clothing.png",
                    name: 'clothing',
                    price: 100,
                    duration: 50,
                    order: 5
                  }],
        'month-4':[{
            ico: "/public/images/game/operations/irrigation.png",
            name: 'irrigation',
            price: 100,
            duration: -1,
            order: 1
          }, {
              ico: "/public/images/game/operations/plowing.png",
              name: 'plowing',
              price: 2000,
              duration: 20,
              order: 2
            },{
                ico: "/public/images/game/operations/spraying.png",
                name: 'spraying',
                requires: 'spraying',
                price: 0,
                duration: 30,
                order: 4
              },{
                  ico: "/public/images/game/operations/fertilizing.png",
                  name: 'fertilizing',
                  price: 1000,
                  duration: 10,
                  requires: 'fertilizer',
                  order: 5
                },{
                    ico: "/public/images/game/operations/clothing.png",
                    name: 'clothing',
                    price: 100,
                    duration: 50,
                    order: 4
                  }],
        'month-5':[{
            ico: "/public/images/game/operations/irrigation.png",
            name: 'irrigation',
            price: 100,
            duration: -1,
            order: 1
          }, {
              ico: "/public/images/game/operations/plowing.png",
              name: 'plowing',
              price: 2000,
              duration: 20,
              order: 2
            },{
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires:'digging',
                order: 3
              },{
                  ico: "/public/images/game/operations/spraying.png",
                  name: 'spraying',
                  requires: 'spraying',
                  price: 0,
                  duration: 30,
                  order: 4
                },{
                    ico: "/public/images/game/operations/fertilizing.png",
                    name: 'fertilizing',
                    price: 1000,
                    duration: 10,
                    requires: 'fertilizer',
                    order: 5
                  }],
        'month-6':[{
            ico: "/public/images/game/operations/irrigation.png",
            name: 'irrigation',
            price: 100,
            duration: -1,
            order: 1
          }, {
              ico: "/public/images/game/operations/plowing.png",
              name: 'plowing',
              price: 2000,
              duration: 20,
              order: 2
            },{
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires:'digging',
                order: 3
              },{
                  ico: "/public/images/game/operations/spraying.png",
                  name: 'spraying',
                  requires: 'spraying',
                  price: 0,
                  duration: 30,
                  order: 4
                },{
                    ico: "/public/images/game/operations/fertilizing.png",
                    name: 'fertilizing',
                    price: 1000,
                    duration: 10,
                    requires: 'fertilizer',
                    order: 5
                  }],
        'month-7':[{
            ico: "/public/images/game/operations/irrigation.png",
            name: 'irrigation',
            price: 100,
            duration: -1,
            order: 1
          }, {
              ico: "/public/images/game/operations/plowing.png",
              name: 'plowing',
              price: 2000,
              duration: 20,
              order: 2
            },{
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires:'digging',
                order: 3
              },{
                  ico: "/public/images/game/operations/spraying.png",
                  name: 'spraying',
                  requires: 'spraying',
                  price: 0,
                  duration: 30,
                  order: 4
                },{
                    ico: "/public/images/game/operations/fertilizing.png",
                    name: 'fertilizing',
                    price: 1000,
                    duration: 10,
                    requires: 'fertilizer',
                    order: 5
                  }],
        'month-8':[{
            ico: "/public/images/game/operations/irrigation.png",
            name: 'irrigation',
            price: 100,
            duration: -1,
            order: 1
          }, {
              ico: "/public/images/game/operations/plowing.png",
              name: 'plowing',
              price: 2000,
              duration: 20,
              order: 2
            },{
                ico: "/public/images/game/operations/digging.png",
                name: 'digging',
                price: 100,
                duration: 50,
                requires:'digging',
                order: 3
              },{
                  ico: "/public/images/game/operations/spraying.png",
                  name: 'spraying',
                  requires: 'spraying',
                  price: 0,
                  duration: 30,
                  order: 4
                },{
                    ico: "/public/images/game/operations/fertilizing.png",
                    name: 'fertilizing',
                    price: 1000,
                    duration: 10,
                    requires: 'fertilizer',
                    order: 5
                  }],
        'month-9':[{
            ico: "/public/images/game/operations/harvest.png",
            name: 'harvest',
            price: 100,
            duration: 100,
            order: 1
          },{
              ico: "/public/images/game/operations/irrigation.png",
              name: 'irrigation',
              price: 100,
              duration: -1,
              order: 1
            }, {
                ico: "/public/images/game/operations/plowing.png",
                name: 'plowing',
                price: 2000,
                duration: 20,
                order: 2
              },{
                  ico: "/public/images/game/operations/digging.png",
                  name: 'digging',
                  price: 100,
                  duration: 50,
                  requires:'digging',
                  order: 3
                },{
                    ico: "/public/images/game/operations/spraying.png",
                    name: 'spraying',
                    requires: 'spraying',
                    price: 0,
                    duration: 30,
                    order: 4
                  },{
                      ico: "/public/images/game/operations/fertilizing.png",
                      name: 'fertilizing',
                      price: 1000,
                      duration: 10,
                      requires: 'fertilizer',
                      order: 5
                    }],
        'month-10':[{
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
            order: 2
          },{
              ico: "/public/images/game/operations/fertilizing.png",
              name: 'fertilizing',
              price: 1000,
              duration: 10,
              requires: 'fertilizer',
              order: 5
            }],
        'month-11':[ {
            ico: "/public/images/game/operations/plowing.png",
            name: 'plowing',
            price: 2000,
            duration: 20,
            order: 2
          },{
              ico: "/public/images/game/operations/digging.png",
              name: 'digging',
              price: 100,
              duration: 50,
              requires:'digging',
              order: 3
            }],
        'month-12':[{
        	
        }],
        'season-4': [{
          ico: "/public/images/game/operations/irrigation.png",
          name: 'irrigation',
          price: 100,
          duration: -1,
          order: 1
        }, {
          ico: "/public/images/game/operations/plowing.png",
          name: 'plowing',
          price: 2000,
          duration: 20,
          order: 2
        }, {
          ico: "/public/images/game/operations/digging.png",
          name: 'digging',
          price: 100,
          duration: 50,
          requires:'digging',
          order: 3
        }, {
          ico: "/public/images/game/operations/spraying.png",
          name: 'spraying',
          requires: 'spraying',
          price: 0,
          duration: 30,
          order: 4
        }, {
          ico: "/public/images/game/operations/fertilizing.png",
          name: 'fertilizing',
          price: 1000,
          duration: 10,
          requires: 'fertilizer',
          order: 5
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
          order: 1
        }, {
          ico: "/public/images/game/operations/fertilizing.png",
          name: 'fertilizing',
          price: 1000,
          duration: 10,
          requires: 'fertilizer',
          order: 2
        }, {
          ico: "/public/images/game/operations/plowing.png",
          name: 'plowing',
          price: 2000,
          duration: 20,
          order: 3
        }, {
          ico: "/public/images/game/operations/digging.png",
          name: 'digging',
          price: 100,
          duration: 50,
          requires:'digging',
          order: 4
        }, {
          ico: "/public/images/game/operations/irrigation.png",
          name: 'irrigation',
          price: 100,
          duration: -1,
          order: 5
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
          order: 2
        }, {
          ico: "/public/images/game/operations/fertilizing.png",
          name: 'fertilizing',
          price: 1000,
          duration: 10,
          requires: 'fertilizer',
          order: 3
        }, {
          ico: "/public/images/game/operations/digging.png",
          name: 'digging',
          price: 100,
          duration: 50,
          requires:'digging',
          order: 4
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
          order: 6
        }],
        'season-1': [{
          ico: "/public/images/game/operations/plowing.png",
          name: 'plowing',
          price: 2000,
          duration: 20,
          order: 1
        }, {
          ico: "/public/images/game/operations/fertilizing.png",
          name: 'fertilizing',
          price: 1000,
          duration: 10,
          requires: 'fertilizer',
          order: 2
        }, {
          ico: "/public/images/game/operations/clothing.png",
          name: 'clothing',
          price: 100,
          duration: 50,
          order: 4
        }]
      };
    }]);