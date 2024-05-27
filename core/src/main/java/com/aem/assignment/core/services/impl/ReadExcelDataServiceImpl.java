package com.aem.assignment.core.services.impl;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.aem.assignment.core.CommonConstants;
import com.aem.assignment.core.entities.MobileEntity;
import com.aem.assignment.core.services.ReadExcelDataService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Component(service = ReadExcelDataService.class)
public class ReadExcelDataServiceImpl implements ReadExcelDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadExcelDataServiceImpl.class);

    private static final String PARENT_PATH = "/content/training-project/mobilenodes";

    @Override
    public void getDataFromExcel(ResourceResolver resolver, String path) {
        Resource resource = resolver.getResource(path);
        List<MobileEntity> mobileEntities = new ArrayList<>();
        try {
            if (resource.isResourceType(CommonConstants.DAM_ASSET)) {
                Asset asset = resource.adaptTo(Asset.class);
                LOGGER.debug("Asset fetched from parent path: {}", asset);

                if (asset.getMimeType()
                        .equalsIgnoreCase(CommonConstants.EXCEL_MIME_TYPE)) {
                    Rendition rendition = asset.getOriginal();
                    InputStream inputStream = rendition
                            .adaptTo(InputStream.class);
                    XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                    XSSFSheet sheet  =workbook.getSheetAt(0);

                    LOGGER.debug("current Excel Sheet that got uploaded {}",sheet);
                    int row = sheet.getLastRowNum();
                    for (int i = 1; i <= row; i++) {
                        XSSFRow currentRow = sheet.getRow(i);
                        if(currentRow!=null){
                            MobileEntity mobileEntity  = new MobileEntity();
                            mobileEntity
                                    .setName(currentRow.getCell(0)
                                            .getStringCellValue());
                            mobileEntity
                                    .setBrand(currentRow.getCell(1)
                                            .getStringCellValue());
                            mobileEntity
                                    .setPrice(currentRow.getCell(2)
                                            .getNumericCellValue());
                            mobileEntities.add(mobileEntity);
                        }

                    }
                    LOGGER.debug("Final mobile entity list: {}",mobileEntities);
                    createNodeFromList(resolver,mobileEntities);
                }
            }
        } catch (IOException e) {
            LOGGER.debug("failed to get data from excel: {}", e);
        }
    }

    private void createNodeFromList(ResourceResolver resolver, List<MobileEntity> mobileEntities){
        try {
            Resource parentResource = resolver.getResource(PARENT_PATH);
            if (parentResource != null) {
                Node parentNode = parentResource.adaptTo(Node.class);
                LOGGER.debug("Parent node under which we need to create further nodes: {}",parentNode);
                if (parentNode != null) {
                    for (MobileEntity mobileEntity : mobileEntities) {
                        String nodeName = "mobile_" + mobileEntity.getName(); // Generate a unique node name
                        Node mobileNode = parentNode.addNode(nodeName, "nt:unstructured");
                        mobileNode.setProperty("name", mobileEntity.getName());
                        mobileNode.setProperty("brand", mobileEntity.getBrand());
                        mobileNode.setProperty("price", mobileEntity.getPrice());
                        resolver.commit();
                    }
                } else {
                    LOGGER.debug("Failed to adapt parent resource to a Node.");
                }
            } else {
                LOGGER.debug("Parent resource not found at path: {} ",PARENT_PATH);
            }
        } catch (RepositoryException | PersistenceException e) {
            LOGGER.debug("Failed to create nodes: {}", e);
        }
    }

}
