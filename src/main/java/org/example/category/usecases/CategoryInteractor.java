package org.example.category.usecases;



import org.example.album.domains.Album;
import org.example.category.adapters.out.presenters.view.resources.CreateCategoryResponseResources;
import org.example.category.domains.Category;
import org.example.category.usecases.dtos.CategoryDto;
import org.example.category.usecases.dtos.CreateCategoryDto;
import org.example.category.usecases.models.CreateCategoryRequestModel;
import org.example.category.usecases.ports.in.CategoryRestUseCase;
import org.example.category.adapters.out.gateways.ds.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryInteractor implements CategoryRestUseCase {

	@Autowired
	private CategoryRepository categoryRepository;
	@Override
	public CreateCategoryResponseResources create(CreateCategoryRequestModel createCategoryRequestModel){
		if (categoryDsGateway.existsByTitle(createCategoryRequestModel.getName())) {
			return categoryPresenter.albumDeleteException();
		}
		if (categoryDsGateway.existsByTitle(createCategoryRequestModel.getName())){
			return categoryPresenter.albumDeleteException();
		}

		Category category = Category.builder()
				.name(createCategoryRequestModel.getName())
				.build();

		CreateCategoryDto createCategoryDto =
				CreateCategoryDto.builder()
						.name(category.getname())
						.build();

		categoryDsGateway.save(createCategoryDto);

		CategoryDto categoryDto = categoryDsGateway.getByName(category.getname());

		CreateCategoryRequestModel createCategoryResponseModel =
				CreateCategoryResponseModel.builder()
						.name(categoryDto.getName())
						.build();

		return categoryPresenter.successResponse(createCategoryResponseModel);
}






















