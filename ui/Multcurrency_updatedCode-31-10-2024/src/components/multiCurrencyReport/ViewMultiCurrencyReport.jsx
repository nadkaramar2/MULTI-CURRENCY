import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "../reports/AutoPagintationReport";
export default function ViewCurrency() {
  const [alldata, setAlldata] = useState([]);
 
  const [searchQuery, setSearchQuery] = useState("");
  const [itemPage, setItemPage] = useState(20);
  const [tcount, setTcount] = useState(1);
  const [totalElement, setTotalElement] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  useEffect(() => {
    viewcurrencytype();
  }, [tcount, itemPage]);
  const viewcurrencytype = async () => {
    try {
      const response = await amsApi.post(
        `multicurrencychargestypemaster/getchargetype`,
        { pageSize: itemPage, pageNumber: tcount }
      );

      if (response.data.code === "S0000") {
        setTotalElement(response.data.totalElementsSize);
        setAlldata(response.data.multiCurrencyChargesTypeMasters);
       
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

   useEffect(() => {
     if (searchQuery === "" || searchQuery === " ") {
       viewcurrencytype();
     }
   }, [searchQuery]);

   const Search = async () => {
     try {
       const response = await amsApi.post(
         `multicurrencychargestypemaster/viewChargesSearch/pagination/20/1`,
         {
           keyword: searchQuery,
         }
       );
       if (response.data.code === "S0000") {
         setAlldata(response.data.chargesTypeSearch);
         
         // handleShowSuccess(response.data.message);
       } else {
         handleShowError(response.data.message);
       }
     } catch (error) {
       // handleShowError(error.response.data.message);
     }
   };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full scale-96">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className=" sm:px-4 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xm  leading-normal ">
                View MultiCurrency Charges
              </p>
              
            </div>
          </div>
        </div>
        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={Search}
        />
        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={totalElement}
          reCallApi={viewcurrencytype}
        /> */}

        <div className="overflow-x-auto relative shadow-md  ">
          <div className="table-wrp block max-h-[27rem] ">
            <table className="w-full text-xs text-left  text-black dark:text-blue-100">
              <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th scope="col" className="py-2.5 px-8 whitespace-nowrap">
                    Charge Type
                  </th>
                  <th scope="col" className="py-2.5 px-8 whitespace-nowrap">
                    Charge Description
                  </th>
                  <th scope="col" className="py-2.5 px-6 whitespace-nowrap ">
                    total Count
                  </th>
                </tr>
              </thead>
              <tbody>
                {alldata.length > 0 ? (
                  <>
                    {alldata.map(
                      ({ chargeType, chargeDescription, totalCount }) => (
                        <tr className="border-b  dark:border-neutral-500">
                          <td className="px-8 py-2.5  whitespace-nowrap ">
                            <div className="text-xs font-medium text-gray-900">
                              {chargeType || "-"}
                            </div>
                          </td>
                          <td className="px-8 py-2.5 whitespace-nowrap ">
                            <div className="text-xs font-medium text-gray-900">
                              {chargeDescription || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap text-center ">
                            <div className="text-xs font-medium text-gray-900">
                              {totalCount || "-"}
                            </div>
                          </td>
                        </tr>
                      )
                    )}
                  </>
                ) : (
                  <tr>
                    <td colSpan="6" className="px-8 py-1 ">
                      <span className="text-sm font-normal text-gray-500">
                        No data available.
                      </span>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* </div> */}
    </AppLayout>
  );
}
