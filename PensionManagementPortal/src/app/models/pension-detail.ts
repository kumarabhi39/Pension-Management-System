export class PensionDetail {
    constructor(
        public pan: String,
        public pensionAmount: number,
        public bankServiceCharge: number,
        public pensionType: String
    ) { }
}
