using my.bookshop as my from '../db/data-model';
using medestination as ext from './external/medestination';


service CatalogService {
    // @readonly entity Books as projection on my.Books;
    entity Books @(restrict: [{
        grant: [
            'READ',
            'WRITE'
        ],
        to   : ['CORSO_RW_SC']
    }]) as projection on my.Books;

    entity DestExternal @(restrict: [{
        grant: [
            'READ',
            'WRITE'
        ],
        to   : ['CORSO_RW_SC']
    }]) as projection on ext.Books;
}
