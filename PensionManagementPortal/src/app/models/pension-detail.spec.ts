import { PensionDetail } from './pension-detail';

describe('PensionDetail', () => {
  it('should create an instance', () => {
    expect(new PensionDetail("", 0.0, 0.0, "")).toBeTruthy();
  });
});