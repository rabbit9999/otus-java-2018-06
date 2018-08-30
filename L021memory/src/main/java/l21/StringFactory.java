package l21;

public class StringFactory implements GeneralObjectFactory{

    private Integer len;

    StringFactory(int len){
        if(len < 0){
            len = 0;
        }
        this.len = len;
    }

    public String get(int ... args){
        if(this.len>0){
            StringBuilder sb = new StringBuilder();
            int limit = this.len;
            if(limit < 0){
                limit = 0;
            }
            for(int i=0; i<limit; i++){
                sb.append('a');
            }
            return sb.toString();
        }
        else{
            return "";
        }
    }
}