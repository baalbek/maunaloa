package maunaloa.controllers.groovy

import maunaloa.controllers.ChartCanvasController
import maunaloa.events.mongodb.FetchFromMongoDBEvent
import maunaloa.events.mongodb.SaveToMongoDBEvent

/**
 * Created by rcs on 1/15/14.
 */
class LevelsController {

    ChartCanvasController parent

    void onSaveToMongoDBEvent(SaveToMongoDBEvent event) {

    }

    void onFetchFromMongoDBEvent(FetchFromMongoDBEvent event) {

    }
}
