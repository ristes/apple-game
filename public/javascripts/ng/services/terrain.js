Game.factory('Terrain', ['$resource', function($resource) {

    var terrains = [{
        id: 1,
        name: 'Blocky',
        url: '/public/images/game/terrain/aglesta.png',
        description: "Blocky soil is composed of small, irregularly sized blocks. It generally has an average supply of organic matter, good water retention, and medium to low water permeability. It has a medium to high content of clay and dust, which is why the soil possesses an excellent capacity for organic matter retention. It is susceptible to intense erosion and is harder to cultivate."
    }, {
        id: 2,
        name: 'Platy',
        url: '/public/images/game/terrain/lisesta.png',
        description: "Platy soil is composed of particles combined into sheets or plates. It has a low to medium supply of organic matter. It has average water retention and water permeability, coupled with good capability of organic matter retention. It is favorable for cultivation and it is susceptible to erosion."
    }, {
        id: 3,
        name: 'Granular',
        url: '/public/images/game/terrain/prashkasta.png',
        description: "Granular soil is composed of small grains. It has a low to medium supply of organic matter, average water retention and good water permeability. It has average to low capacity for organic matter retention due to the low content of clay and organic matter. It has a light mechanic composition, and it is not susceptible to erosion."
    }, {
        id: 4,
        name: 'Prismatic',
        url: '/public/images/game/terrain/prizmaticna.png',
        description: "Prismatic soil is composed of pillars separated by small cracks. It has a medium supply of organic matter, good water retention and low water permeability. It has excellent capacity for nutrients retention due to the high content of clay and dust. It is susceptible to intense erosion in sloping terrain. It is very hard to cultivate. "
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