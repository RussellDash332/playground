class Mutant:
    def __init__(self,name):
        self.name = name
        self.master = self
        self.mastermind = self
        self.strength = 0
        self.slave = None # placebo variable

    def get_name(self):
        return self.name

    def get_master(self):
        return self.master

    def get_mastermind(self):
        return self.mastermind

class Telepath(Mutant):
    def __init__(self,name,strength):
        self.name = name
        self.strength = strength
        self.slave = None
        self.master = self
        self.mastermind = self

    def get_slave(self):
        return self.slave

    def mind_control(self,m):
        # causes the telepath to mind-control the Mutant (or Telepath) m.
        # If the telepath does not have a slave, then the telepath mind-controls m directly.
        # Otherwise, the telepath uses the last slave in his chain of slaves to do the mind-controlling, if that last slave is a telepath.
        # If the last slave is a non-telepath mutatnt, nothing happens.
        # The success of the mind-control attempt is contingent on the rules above.

        # no cyclic attempt!
        if m.mastermind != self:
            if self.strength > m.strength: # compulsory condition
                # check if self has no master, otherwise self cannot act
                if self.master == self:
                    # check if self has no slave
                    if self.slave == None:
                        # check if m has a master
                        if m.master != m:
                            # steal m from the master
                            self.slave, m.master.slave, m.master = m, None, self

                            # change all masterminds of m's slaves into self
                            minichain = m
                            while minichain.slave != None:
                                minichain.mastermind = self
                                minichain = minichain.slave
                            minichain.mastermind = self
                            
                        # m has no master, so direct conrol
                        else:
                            # change all masterminds of m's slaves into self
                            minichain = m
                            while minichain.slave != None:
                                minichain.mastermind = self
                                minichain = minichain.slave
                            minichain.mastermind = self

                            self.slave, m.master = m, self
                        
                    # self has a chain of slave(s)
                    else:
                        # find the last slave in the chain
                        chain = self.slave
                        while chain.slave != None:
                            chain = chain.slave

                        # do the chain mind control only if chain is a telepath 
                        if isinstance(chain,Telepath):
                            # change all masterminds of m's slaves into self
                            minichain = m
                            while minichain.slave != None:
                                minichain.mastermind = self
                                minichain = minichain.slave
                            minichain.mastermind = self
                            
                            chain.slave, m.master = m, chain

    def release(self,*m):
        # causes the telepath to release the Mutant (or Telepath) m from mindcontrol.
        # If release is called with zero arguments, the telepath releases all its slaves.
        # No action is taken if m is not one of the telepath’s slaves to begin with.
        # Releasing a mindcontrolled telepath who controls some other telepath or mutant will also free the controlled mutant as well.

        # check if m is a tuple of a specific person, not an empty tuple
        if m:
            m = m[0]
            if self.strength > m.strength: # compulsory condition
                # check if self has no master, otherwise self cannot act
                if self.master == self:
                    # change all masterminds and masters of m's slaves into themselves
                    # change all slaves of m's slaves into None
                    # remember that m's master must have no more slaves
                    minichain = m
                    minichain.master.slave = None
                    while minichain.slave != None:
                        minichain.slave, minichain.mastermind, minichain.master, minichain = None, minichain, minichain, minichain.slave
                    minichain.mastermind, minichain.master = minichain, minichain

        # release everyone!
        else:
            m = self.slave
            # releaser can have no slave, hence None
            if m:
                if self.strength > m.strength: # compulsory condition
                    # check if self has no master, otherwise self cannot act
                    if self.master == self:
                        # change all masterminds and masters of m's slaves into themselves
                        # change all slaves of m's slaves into None
                        # remember that m's master must have no more slaves
                        minichain = m
                        minichain.master.slave = None
                        while minichain.slave != None:
                            minichain.slave, minichain.mastermind, minichain.master, minichain = None, minichain, minichain, minichain.slave
                        minichain.mastermind, minichain.master = minichain, minichain

def test_q3():
    print("Testing Q3")

    logan = Mutant("Wolverine")
    charles = Telepath("Prof. X", 10)
    jean = Telepath("Phoenix", 9)
    psylocke = Telepath("Psylocke", 8)
    emma = Telepath("Frost", 7)

    def breakdown():
        print("##### CURRENT BREAKDOWN #####")
        xmen = (charles,jean,psylocke,emma,logan)
        for i in xmen:
            print(i.get_name())
            t = i.slave.get_name() if i.slave else i.slave
            print('slave:',t)
            print('master:',i.master.get_name())
            print('mastermind:',i.mastermind.get_name())
            print()
        print("####################")

    # === psylocke controls logan ===
    print('=== psylocke controls logan ===')
    psylocke.mind_control(logan) # [P -> L]
    print(logan.get_master().get_name() == "Psylocke")
    print(psylocke.get_slave().get_name() == "Wolverine")

    # === jean steals logan from psylocke ===
    print('=== jean steals logan from psylocke ===')
    jean.mind_control(logan) # [J -> L]
    print(logan.get_master().get_name() == "Phoenix")
    print(jean.get_slave().get_name() == "Wolverine")
    print(psylocke.get_slave() == None)

    print('=== charles mind controls Jean ===')
    charles.mind_control(jean) # [C -> J -> L]
    print(logan.get_master().get_name() == "Phoenix")
    print(jean.get_master().get_name() == "Prof. X")
    print(charles.get_slave().get_name() == "Phoenix")

    # === psylocke fails to steal jean ===
    print('=== psylocke fails to steal jean ===')
    psylocke.mind_control(jean) # [C -> J -> L]
    print(jean.get_master().get_name() == "Prof. X")

    # === psylocke fails to control charles ===
    print('=== psylocke fails to control charles ===')
    psylocke.mind_control(charles) # [C -> J -> L]
    print(charles.get_master().get_name() == "Prof. X")

    # === charles releases logan ===
    print("=== charles releases logan ===")
    charles.release(logan) # [C -> J]
    print(charles.get_slave().get_name() == "Phoenix")
    print(jean.get_slave() == None)
    print(logan.get_master().get_name() == "Wolverine")

    # === psylocke controls emma ===
    print("=== psylocke controls emma ===")
    psylocke.mind_control(emma) # [P -> E, C -> J]
    print(emma.get_master().get_name() == "Psylocke")

    ##print(psylocke.get_master().get_name())

    # === charles controls psylocke ===
    print("=== charles controls psylocke ===")
    charles.mind_control(psylocke) # [C -> J -> P -> E]
    
    print(emma.get_mastermind().get_name() == "Prof. X")
    print(psylocke.get_mastermind().get_name() == "Prof. X")
    print(jean.get_mastermind().get_name() == "Prof. X")
    print(charles.get_mastermind().get_name() == "Prof. X")

    ##print(psylocke.get_master().get_name())

    # === jean cannot release anybody ===
    print("=== jean cannot release anybody ===")
    jean.release(psylocke) # [C -> J -> P -> E]
    print(psylocke.get_master().get_name() == "Phoenix")
    
    ##print(psylocke.get_master().get_name())

    # === emma cannot act ===
    print("=== emma cannot act ===")
    emma.mind_control(logan) # [C -> J -> P -> E]
    print(logan.get_master().get_name() == "Wolverine")

    # === failed cyclic control attempt ===
    print("=== failed cyclic control attempt ===")
    charles.mind_control(jean) # [C -> J -> P -> E]
    print(emma.get_slave() == None)
    
    # === release all ===
    print("=== release all ===")
    charles.release(jean)
    print(charles.get_master().get_name() == "Prof. X")
    print(jean.get_master().get_name() == "Phoenix")
    print(emma.get_master().get_name() == "Frost")
    print(psylocke.get_master().get_name() == "Psylocke")
    print(logan.get_master().get_name() == "Wolverine")
    print("=======")

# Uncomment to test question 3
test_q3()

human = Mutant("Human")
ten = Telepath("10-Bit", 10)
nine = Telepath("9-Bit", 9)
eight = Telepath("8-Bit", 8)
seven = Telepath("7-Bit", 7)

def breakdown():
    print("##### CURRENT BREAKDOWN #####")
    test = (ten,nine,eight,seven,human)
    for i in test:
        print(i.get_name())
        t = i.slave.get_name() if i.slave else i.slave
        print('slave:',t)
        print('master:',i.master.get_name())
        print('mastermind:',i.mastermind.get_name())
        print()
    print("####################")
