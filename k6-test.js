import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

const businessFailures = new Rate('business_req_failed');

export const options = {
  stages: [
    { duration: '30s', target: 100 },
    { duration: '1m', target: 300 },
    { duration: '1m', target: 700 },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    business_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<500'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8082';

export default function () {
  const userId = Math.floor(Math.random() * 8) + 1;
  const res = http.post(`${BASE_URL}/order/createOrderByCrud/${userId}`);
  let ok = false;
  try {
    ok = res.status === 200 && res.json('code') === 200;
  } catch (e) {
    ok = false;
  }
  businessFailures.add(!ok);
  check(res, {
    'http status is 200': (r) => r.status === 200,
    'business code is 200': () => ok,
  });
  sleep(0.1);
}
