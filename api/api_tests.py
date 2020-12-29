# This file contains the unit tests for the API
import unittest

import requests

url = 'http://127.0.0.1:5000'


class TestCalc(unittest.TestCase):

    # Tests for checking if get functions properly
    def test_get_user(self):
        api_request = requests.get(url + '/get/user/' + 'yreddy').json()
        self.assertEqual(api_request['name'],
                         "Yadu Reddy")

    def test_get_progress(self):
        api_request = requests.get(url + '/get/progress/' + 'yreddy').json()
        self.assertEqual(api_request['progress'], 13.0)

    def test_get_runs(self):
        api_request = requests.get(url + '/get/runs/' + 'yreddy').json()
        self.assertEqual(api_request, [3.2, 4.5, 2.1, 3.2])

    def test_put_runs(self):
        element_count = len(requests.get(url + '/get/runs/' + 'test').json())
        element_count += 1
        payload = {"run": 1.0}
        requests.put(url + '/add/run/' + 'test', json=payload)
        self.assertEqual(len(requests.get(url + '/get/runs/' + 'test').json()), element_count)

    def test_put_runs_progress(self):
        element_count = requests.get(url + '/get/progress/' + 'test').json()['progress']
        element_count += 1
        payload = {"run": 1.0}
        requests.put(url + '/add/run/' + 'test', json=payload)
        self.assertEqual(requests.get(url + '/get/progress/' + 'test').json()['progress'], element_count)

    def test_put_bad_friends(self):
        element_count = len(requests.get(url + '/get/friends/' + 'test').json())
        payload = {"friend": "none"}
        requests.put(url + '/add/friend/' + 'test', json=payload)
        self.assertEqual(len(requests.get(url + '/get/friends/' + 'test').json()), element_count)

    def test_put_good_friends(self):
        element_count = len(requests.get(url + '/get/friends/' + 'test').json())
        element_count += 1
        payload = {"friend": 'yreddy'}
        r = requests.put(url + '/add/friend/' + 'test', json=payload)
        print(r.status_code)
        self.assertEqual(len(requests.get(url + '/get/friends/' + 'test').json()), element_count)

    def test_put_milestones(self):
        element_count = len(requests.get(url + '/get/milestones/' + 'test').json())
        element_count += 1
        payload = {"milestone": "awardTest"}
        requests.put(url + '/add/milestone/' + 'test', json=payload)
        self.assertEqual(len(requests.get(url + '/get/milestones/' + 'test').json()), element_count)

    def test_get_friends(self):
        api_request = requests.get(url + '/get/friends/' + 'yreddy').json()
        self.assertEqual(api_request, ['2815', '3307', '25201', 'here'])

    def test_get_milestones(self):
        api_request = requests.get(url + '/get/milestones/' + 'yreddy').json()
        self.assertEqual(api_request, ['award1', 'award2'])


if __name__ == '__main__':
    unittest.main()
