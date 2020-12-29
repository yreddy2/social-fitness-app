# This file contains the API that communicates with the database(db)
from flask import Flask

from pymongo import MongoClient

from bson.json_util import dumps

from flask import jsonify, request

# Uses flask to connect the database to the api
app = Flask(__name__)
client = MongoClient('INSERT_DATABASE_HERE')
db = client.get_database('fitness_app')

# set of user documents
user_collection = db.users


# Methods to send signals or errors for the different API calls
def send_ok():
    resp = jsonify("200 OK")
    resp.status_code = 200
    return resp


def send_created():
    resp = jsonify("201 CREATED")
    resp.status_code = 201
    return resp


def send_bad_request():
    resp = jsonify("400 Bad Request")
    resp.status_code = 400
    return resp


def send_unsupported_media_type():
    resp = jsonify("415 Unsupported Media Type")
    resp.status_code = 415
    return resp


def get_record(specifier):
    """
    Method to parse the id of user
    :param specifier: the id of a user
    :return: the record that the db needs to search for
    """
    val = specifier
    record = {"_id": {"$regex": val, "$options": 'i'}}
    return record


def get_single_record(specifier):
    """
    Method to parse the id of user
    :param specifier: the id of a user
    :return: the record that the db needs to search for
    """
    val = specifier
    record = {"_id": val}
    return record


def get_query(specifier):
    """
    Method to get documents from the db
    :param specifier: id of the user
    :return: set of documents that fit the parameter
    """
    try:
        record = get_record(specifier)
    except Exception as e:
        return send_bad_request()
    else:
        ret = user_collection.find(record)
        return dumps(ret)


def get_single_query(specifier):
    """
    Method to get documents from the db
    :param specifier: id of the user
    :return: set of documents that fit the parameter
    """
    try:
        record = get_single_record(specifier)
    except Exception as e:
        return send_bad_request()
    else:
        ret = user_collection.find(record)
        return dumps(ret)


def get_query_runs(specifier):
    """
    Method to get the runs of a certain user
    :param specifier: id of a user
    :return: list of runs of a specific user
    """
    try:
        record = get_single_record(specifier)
    except Exception as e:
        return send_bad_request()
    else:
        ret = user_collection.find_one(record)
        if not len(ret):
            return send_unsupported_media_type()
        return dumps(ret['runs'])


def get_query_progress(specifier):
    """
    Method to get the progress of a user
    :param specifier: id of a user
    :return: Total distance ran of a user
    """
    try:
        record = get_single_record(specifier)
    except Exception as e:
        return send_bad_request()
    else:
        ret = user_collection.find_one(record)
        if not len(ret):
            return send_unsupported_media_type()
        return dumps({"progress": sum(ret['runs'])})


def get_query_friends(specifier):
    """
    Method to get the friends of a certain user
    :param specifier: id of a user
    :return: list of friends of a specific user
    """
    try:
        record = get_single_record(specifier)
    except Exception as e:
        return send_bad_request()
    else:
        ret = user_collection.find_one(record)
        if not len(ret):
            return send_unsupported_media_type()
        return dumps(ret['friends'])


def get_query_milestones(specifier):
    """
    Method to get the milestones of a certain user
    :param specifier: id of a user
    :return: list of milestones of a specific user
    """
    try:
        record = get_single_record(specifier)
    except Exception as e:
        return send_bad_request()
    else:
        ret = user_collection.find_one(record)
        if not len(ret):
            return send_unsupported_media_type()
        return dumps(ret['milestones'])


def add_query():
    """
    Method to add a document to the db
    :return: response
    """
    try:
        info = request.json
    except Exception as e:
        return send_bad_request()
    else:
        for key in info:
            if user_collection.find({key: {"$exists": True}}).count() == 0:
                return send_unsupported_media_type()
        if user_collection.find(get_single_record(info["_id"])).count() > 0:
            return send_bad_request()
        user_collection.insert(info)
        return send_created()


def delete_query(specifier):
    """
    Method to delete a document from the db
    :param specifier: id of a user
    :return: response
    """
    try:
        record = get_record(specifier)
    except Exception as e:
        return send_bad_request()
    else:
        user_collection.delete_one(record)
        return send_ok()


def update_query(specifier):
    """
    Method to update a document in the db
    :param specifier: id of a user
    :return: response
    """
    try:
        updates = request.json
    except Exception as e:
        return send_bad_request()
    else:
        for key in updates:
            if user_collection.find({key: {"$exists": True}}).count() == 0:
                return send_unsupported_media_type()
            user_collection.update_one(get_single_record(specifier), {"$set": {key: updates[key]}})
        return send_ok()


def update_query_run(specifier):
    """
    Method to add a run to a user
    :param specifier: id of a user
    :return: response
    """
    try:
        updates = request.json
    except Exception as e:
        return send_bad_request()
    else:
        if not len(updates):
            return send_unsupported_media_type()
        if 'run' in updates:
            user_collection.update(get_single_record(specifier), {'$push': {'runs': updates['run']}})
            return send_ok()
        return send_unsupported_media_type()


def update_query_friend(specifier):
    """
    Method to add a friend to a user
    :param specifier: id of a user
    :return: response
    """
    try:
        updates = request.json
    except Exception as e:
        return send_bad_request()
    else:
        if not len(updates):
            return send_unsupported_media_type()
        if 'friend' in updates:
            if user_collection.find(get_single_record(updates["friend"])).count() == 0:
                return send_bad_request()
            user = user_collection.find_one(get_single_record(specifier))
            if updates['friend'] in user['friends']:
                return send_bad_request()
            user_collection.update(get_single_record(specifier), {'$push': {'friends': updates['friend']}})
            return send_ok()
        return send_unsupported_media_type()


def update_query_milestone(specifier):
    """
    Method to add a milestone to a user
    :param specifier: id of a user
    :return: response
    """
    try:
        updates = request.json
    except Exception as e:
        return send_bad_request()
    else:
        if not len(updates):
            return send_unsupported_media_type()
        if 'milestone' in updates:
            user_collection.update(get_single_record(specifier), {'$push': {'milestones': updates['milestone']}})
            return send_ok()
        return send_unsupported_media_type()


# Connecting URL's to their associated methods.
@app.route('/get/users/<specifier>')
def get_users(specifier):
    return get_query(specifier)


@app.route('/get/user/<specifier>')
def get_user(specifier):
    return get_single_query(specifier)


@app.route('/get/runs/<specifier>')
def get_runs(specifier):
    return get_query_runs(specifier)


@app.route('/get/friends/<specifier>')
def get_friends(specifier):
    return get_query_friends(specifier)


@app.route('/get/milestones/<specifier>')
def get_milestones(specifier):
    return get_query_milestones(specifier)


@app.route('/get/progress/<specifier>')
def get_progress(specifier):
    return get_query_progress(specifier)


@app.route('/update/user/<specifier>', methods=['PUT'])
def update_user(specifier):
    return update_query(specifier)


@app.route('/add/run/<specifier>', methods=['PUT'])
def add_run(specifier):
    return update_query_run(specifier)


@app.route('/add/friend/<specifier>', methods=['PUT'])
def add_friends(specifier):
    return update_query_friend(specifier)


@app.route('/add/milestone/<specifier>', methods=['PUT'])
def add_milestones(specifier):
    return update_query_milestone(specifier)


@app.route('/add/user', methods=['POST'])
def add_user():
    return add_query()


@app.route('/delete/user/<specifier>', methods=['DELETE'])
def delete_user(specifier):
    return delete_query(specifier)


@app.route('/custom', methods=['PUT'])
def custom():
    """
    Queries the database based on a custom query from a user
    :return: queried result
    """
    try:
        info = request.json
    except Exception as e:
        return send_bad_request()
    else:
        try:
            record = user_collection.aggregate(
                [
                    info
                ]
            )
            return dumps(record)
        except Exception as e:
            return send_unsupported_media_type()


if __name__ == "__main__":
    app.run(debug=True)