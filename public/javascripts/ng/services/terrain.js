Game.factory('Terrain', ['$resource', function($resource) {

    var terrains = [{
        id: 1,
        name: 'aglesta',
        url: '/public/images/game/terrain/aglesta.png'
    }, {
        id: 2,
        name: 'lisesta',
        url: '/public/images/game/terrain/lisesta.png'
    }, {
        id: 3,
        name: 'prashkasta',
        url: '/public/images/game/terrain/prashkasta.png'
    }, {
        id: 4,
        name: 'prizmaticna',
        url: '/public/images/game/terrain/prizmaticna.png'
    }];

    var terrainSizes = [{
        size: 4,
        name: '4 Ha',
        price: 80000,
        url: '/public/images/game/pocva-prodavnica-icon.png',
        description: '4 Ha'
    }, {
        size: 2,
        name: '2 Ha',
        price: 40000,
        url: '/public/images/game/pocva-prodavnica-icon.png',
        description: '2 Ha'
    }, {
        size: 1,
        name: '1 Ha',
        price: 20000,
        url: '/public/images/game/pocva-prodavnica-icon.png',
        description: '1 Ha'
    }, {
        size: 0.5,
        name: '0.5 Ha',
        price: 10000,
        url: '/public/images/game/pocva-prodavnica-icon.png',
        description: '0.5 Ha'
    }];

    var res = $resource('', {}, {
        'buyTerrain': {
            method: 'POST',
            url: '/terrainshop/buyTerrain'
        }
    });

    return {
        terrains: terrains,
        terrainSizes: terrainSizes,
        buyTerrain: res.buyTerrain
    };

}]);