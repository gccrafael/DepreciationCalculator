/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import javax.swing.JRadioButton;

/**
 *
 * Rafael Garcia
 */
public class Asset 
{
    private String name, errmsg;
    private double cost, salvage, straightDep, doubledeclinerate;
    private double[][] begbal, endbal;
    private double[] anndep;
    private boolean built;
    private int life;
    
    public Asset()
    { 
        name = "";
        cost = 0;
        salvage = 0;
        life = 0;
        errmsg = "";
        built = false;
    }
    
    public Asset(String name, double cost, double salvage, int life)
    {
        this.name = name;
        this.cost = cost;
        this.salvage = salvage;
        this.life = life;
        
        if (isValid())
        {
            calcDep();            
        }        
    }
    private boolean isValid()
    {
        this.errmsg = "";
        if (this.cost <= 0)
        {
            errmsg += "cost not a positive value;";
        }    
        else if (this.salvage >= this.cost)
        {
            errmsg += "Salvage must less than cost;";
        }    
        if (this.life < 1)
        {
            errmsg += "Life must 1 year or more;";
        }    
        if (errmsg.isEmpty())
        {
            return true;
        } 
        return false;
    }        
    private void calcDep()
    {
        built = false;
        if (!isValid())
        {
            return;
        }   
        straightDep = (this.cost - this.salvage) / this.life;
        doubledeclinerate = (1.0 / this.life) * 2.0;
        
        begbal = new double[this.life][2]; //col 0 = SL, 1 = DDL
        endbal = new double[this.life][2];
        anndep = new double[this.life]; // DDL values onlye
        
        begbal[0][0] = this.cost; //SL start value
        endbal[0][0] = this.cost;
        begbal[0][1] = this.cost; // DDL start value        
        anndep[0] = this.cost * doubledeclinerate;
        endbal[0][1] = this.cost - anndep[0];
        for (int i = 1; i < this.life; i++)
        {
            // calculate values for SL and DDL
            //SL
            begbal[i][0] = begbal[i-1][0] - straightDep;
            endbal[i-1][0] = begbal[i-1][0] - straightDep;
            endbal[this.life-1][0] = this.salvage;
            //DDL
            if ((endbal[i-1][1] * doubledeclinerate) > straightDep) {
                begbal[i][1] = endbal[i-1][1];
                anndep[i] = begbal[i][1] * doubledeclinerate;
                endbal[i][1] = begbal[i][1] - anndep[i];
            }
            else if ((endbal[i-1][1] * doubledeclinerate) < straightDep && 
                    (endbal[i-1][1] > salvage) && 
                    (endbal[i-1][1] - straightDep > salvage)) {
                begbal[i][1] = endbal[i-1][1];
                anndep[i] = straightDep;
                endbal[i][1] = begbal[i][1] - anndep[i];
            }
            else if ((endbal[i-1][1] * doubledeclinerate) < straightDep && 
                    (endbal[i-1][1] > salvage) && 
                    (endbal[i-1][1] - straightDep < salvage)) {
                begbal[i][1] = endbal[i-1][1];
                anndep[i] = begbal[i][1] - salvage;
                endbal[i][1] = salvage; 
            }           
            else {
                begbal[i][1] = endbal[i-1][1];
                anndep[i] = 0;
                endbal[i][1] = salvage;
            }
        }              
        built = true;        
    }        
    public String getErrorMsg()
    {
        return errmsg;
    }   
    
    public String getAsset() {
        return name;
    }

    public void setAsset(String asset) {
        this.name = asset;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getSalvage() {
        return salvage;
    }

    public void setSalvage(double salvage) {
        this.salvage = salvage;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
    public double getAnnDep()
    {
        //returns SL DEP value (equal in all yrs)
        if (!built)
        {
            calcDep();
        }    
        if (!errmsg.isEmpty())
        {
            return -1;
        }  
        return this.straightDep;
      
        
    }
    
    public double getAnnDep(int y)
    {
        //returns DDL DEP value from anndep[]
        if (!built)
        {
            calcDep();
        }    
        if (!errmsg.isEmpty())
        {
                return -1;
        }    
        if (y < 0 || y > this.life)
        {
                return -1;
        }    
        return this.anndep[y];
                      
        
    }        
    
    public double getBegBal(int i, int radiobuttonL0orR1)
    {
        //returns SL DEP value (equal in all yrs)
        if (!built)
        {
            calcDep();
        }    
        if (!errmsg.isEmpty())
        {
            return -1;
        }  
        return this.begbal[i][radiobuttonL0orR1];    
    } 
    
    public double getEndBal(int i, int radiobuttonL0orR1)
    {
        //returns SL DEP value (equal in all yrs)
        if (!built)
        {
            calcDep();
        }    
        if (!errmsg.isEmpty())
        {
            return -1;
        }  
        return this.endbal[i][radiobuttonL0orR1];    
    } 
    /*
    @Override
    public double[][] toString()
    {
        return this.begbal;
    }*/
}
