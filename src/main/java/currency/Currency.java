package currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Currency {
        /**
         *
         "id": 22,
         "Code": "826",
         "Ccy": "GBP",
         "CcyNm_RU": "Фунт стерлингов",
         "CcyNm_UZ": "Angliya funt sterlingi",
         "CcyNm_UZC": "Англия фунт стерлинги",
         "CcyNm_EN": "Pound Sterling",
         "Nominal": "1",
         "Rate": "14360.78",
         "Diff": "21.52",
         "Date": "16.12.2021"
         */
        public int id;
        public  String Code;
        public String Ccy;
        public String CcyNm_RU;
        public String CcyNm_UZ;
        public String CcyNm_UZC;
        public String CcyNm_EN;
        public String Nominal;
        public String Rate;
        public String Diff;
        public String  Date;





}
