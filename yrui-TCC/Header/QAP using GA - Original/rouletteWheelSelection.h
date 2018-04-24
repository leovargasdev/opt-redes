int RoulettteWhellSelectionO(double *P) {
	int i;

	double valorRand;
	double somaP = 0.0;
	valorRand = ((double)rand() / (double)RAND_MAX);

	//printf("\n rand %f \n", valorRand);
	for (i = 0; i < nPop; i++) {
		somaP += P[i];
		//printf("\nSOMAP%f\n", somaP);
		if (somaP > valorRand) {
			//printf("\n I ESCOLHIDO %d\n", i);
			//system("pause");
			return i;
		}
	}

	return i - 1;
}